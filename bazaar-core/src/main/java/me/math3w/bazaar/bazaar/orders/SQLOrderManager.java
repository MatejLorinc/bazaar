package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import me.math3w.bazaar.databases.MySQLDatabase;
import me.math3w.bazaar.databases.SQLDatabase;
import me.math3w.bazaar.databases.SQLiteDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class SQLOrderManager extends AbstractOrderManager {
    private final SQLDatabase sqlDatabase;

    public SQLOrderManager(BazaarPlugin bazaarPlugin) {
        super(bazaarPlugin);

        SQLDatabase.Type sqlType = bazaarPlugin.getDatabaseConfig().getSqlType();
        if (sqlType == SQLDatabase.Type.MYSQL) {
            sqlDatabase = new MySQLDatabase(bazaarPlugin.getDatabaseConfig().getMySQLConfig());
        } else {
            sqlDatabase = new SQLiteDatabase(bazaarPlugin.getDataFolder(), "orders");
        }
        createTable();
    }

    private void createTable() {
        String autoIncrementSyntax = sqlDatabase.getType() == SQLDatabase.Type.SQLITE ? "AUTOINCREMENT" : "AUTO_INCREMENT";

        try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY " + autoIncrementSyntax + ", " +
                "product_id VARCHAR(32) NOT NULL, " +
                "amount INTEGER NOT NULL, " +
                "unit_price DECIMAL NOT NULL, " +
                "order_type VARCHAR(16) NOT NULL, " +
                "player VARCHAR(36) NOT NULL, " +
                "filled INTEGER DEFAULT 0, " +
                "claimed INTEGER DEFAULT 0, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<SubmitResult> registerBazaarOrder(BazaarOrder order) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("INSERT INTO orders (product_id, amount, unit_price, order_type, player, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, order.getProduct().getId());
                statement.setInt(2, order.getAmount());
                statement.setDouble(3, order.getUnitPrice());
                statement.setString(4, order.getType().name());
                statement.setString(5, String.valueOf(order.getPlayer()));
                statement.setTimestamp(6, Timestamp.from(order.getCreatedAt()));
                statement.executeUpdate();
                return SubmitResult.SUCCESS;
            } catch (SQLException e) {
                e.printStackTrace();
                return SubmitResult.ERROR;
            }
        });
    }

    @Override
    public CompletableFuture<List<BazaarOrder>> getBuyOrders(Product product, Predicate<List<BazaarOrder>> shouldContinuePredicate) {
        return getOrders(product, OrderType.BUY, shouldContinuePredicate);
    }

    @Override
    public CompletableFuture<List<BazaarOrder>> getSellOffers(Product product, Predicate<List<BazaarOrder>> shouldContinuePredicate) {
        return getOrders(product, OrderType.SELL, shouldContinuePredicate);
    }

    private CompletableFuture<List<BazaarOrder>> getOrders(Product product, OrderType type, Predicate<List<BazaarOrder>> shouldContinuePredicate) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("SELECT * FROM orders WHERE product_id=? AND order_type=? AND filled<amount")) {
                statement.setString(1, product.getId());
                statement.setString(2, type.name());

                List<BazaarOrder> orders = new ArrayList<>();
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        orders.add(new DefaultBazaarOrder(product,
                                resultSet.getInt("amount"),
                                resultSet.getDouble("unit_price"),
                                type,
                                UUID.fromString(resultSet.getString("player")),
                                resultSet.getInt("filled"),
                                resultSet.getInt("claimed"),
                                resultSet.getTimestamp("created_at").toInstant()));
                        if (!shouldContinuePredicate.test(orders)) {
                            break;
                        }
                    }
                }

                return orders;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
