package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.bazaar.BazaarImpl;
import me.zort.containr.Component;
import me.zort.containr.GUI;

public class BazaarMenu {
    private final Bazaar bazaar;

    public BazaarMenu(BazaarImpl bazaar) {
        this.bazaar = bazaar;
    }

    public GUI getMenu(Category selectedCategory) {
        return Component.gui()
                .title("Bazaar")
                .rows(6)
                .prepare((gui, player) -> {
                    gui.setContainer(0, Component.staticContainer()
                            .size(1, 5)
                            .init(container -> {
                                for (Category category : bazaar.getCategories()) {
                                    container.appendElement(Component.element()
                                            .click(element -> {
                                                getMenu(category).open(player);
                                            })
                                            .item(category.getIcon())
                                            .build());
                                }
                            })
                            .build());
                })
                .build();
    }
}
