package sales.controller;

import sales.Price;
import sales.catalog.ProductCatalog;
import sales.display.Display;

public class SalesController {
    private final Display display;
    private final ProductCatalog catalog;

    public SalesController(Display display, ProductCatalog catalog) {
        this.display = display;
        this.catalog = catalog;
    }

    public void onBarCode(String barCode) {

        if (barCode == null || barCode.trim().isEmpty()) {
            display.displayScanAgain();
            return;
        }

        Price price = catalog.findPriceAsCents(barCode);
        if (price == null) {
            display.displayProductNotFound(barCode);
        } else {
            display.displayPrice(price);
        }
    }
}