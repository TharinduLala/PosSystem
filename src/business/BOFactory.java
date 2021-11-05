package business;

import business.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBOFactory() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public SuperBO getBO(BoTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case PLACEORDER:
                return new PlaceOrderBOImpl();
            case MANAGEORDER:
                return new ManageOrderBOImpl();
            case CASHIERDASHBOARD:
                return new CashierDashBoardBOImpl();
            case ALLORDERS:
                return new AllOrdersBOImpl();
            default:
                return null;
        }
    }

    public enum BoTypes {
        CUSTOMER, ITEM,PLACEORDER,MANAGEORDER,CASHIERDASHBOARD,ALLORDERS
    }
}



