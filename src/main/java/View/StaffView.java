package View;

import Controller.CustomerController;
import Model.AddressDTO;
import Model.CustomerDTO;
import dbConn.ConnectionMaker;
import Controller.StaffController;
import Model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class StaffView {
    private final Scanner SCANNER;
    private Connection connection;
    private StaffDTO logIn;

    public StaffView(ConnectionMaker connectionMaker) {
        SCANNER = new Scanner(System.in);
        connection = connectionMaker.makeConnection();
    }

    public void showIndex() {
        String message = "1. 로그인 2. 종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message);
            if (userChoice == 1) {
                auth();
                if (logIn != null && logIn.getActive() == 1) {
                    showMenu();
                } else if(logIn != null && logIn.getActive() == 2){
                    showAdmin();
                }
            } else if (userChoice == 2) {
                System.out.println("사용해주셔서 감사합니다.");
                break;
            }
        }
    }

    /*
    private void register() {
        ArrayList<AddressDTO> list;
        ArrayList<StoreDTO> store;
        int address_id = 0;
        boolean select = false;
        String address;
        String message;
        message = "사용하실 아이디를 입력해주세요.";

        StaffDTO u = new StaffDTO();
        u.setUsername(ScannerUtil.nextLine(SCANNER, message));

        message = "사용하실 비밀번호를 입력해주세요.";
        u.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "성을 입력해주세요.";
        u.setFirst_name(ScannerUtil.nextLine(SCANNER, message));

        message = "이름을 입력해주세요.";
        u.setLast_name(ScannerUtil.nextLine(SCANNER, message));

        message = "이메일을 입력해주세요.";
        u.setEmail(ScannerUtil.nextLine(SCANNER, message));

        StaffController staffController = new StaffController(connection);

        while(staffController.duplicateEmail(u.getEmail())){
            message = "중복된 이메일 주소입니다. 다른 이메일을 입력해주세요.";
            u.setEmail(ScannerUtil.nextLine(SCANNER, message));
        }

        message = "검색할 주소를 입력해주세요.";
        address = ScannerUtil.nextLine(SCANNER, message);

        list = staffController.getAddressId(address);

        while(list == null){
            message = "입력한 주소지는 존재하지 않습니다. 주소를 다시 입력해주세요.";
            address = ScannerUtil.nextLine(SCANNER, message);
            list = staffController.getAddressId(address);
        }

        while(!select) {
            for(AddressDTO a : list){
                System.out.println(a.getAddress_id()+". "+a.getAddress());
            }

            message = "주소를 선택해주세요.";
            address_id = ScannerUtil.nextInt(SCANNER, message);

            for (AddressDTO a : list) {
                if (address_id == a.getAddress_id()) {
                    select = true;
                }
            }
        }

        select = false;

        store = staffController.getStoreList();

        while(!select) {
            for(StoreDTO a : store){
                System.out.println(a.getAddress_id()+". "+);
            }

            message = "주소를 선택해주세요.";
            address_id = ScannerUtil.nextInt(SCANNER, message);

            for (AddressDTO a : list) {
                if (address_id == a.getAddress_id()) {
                    select = true;
                }
            }
        }

        if (!staffController.insert(u,address_id)) {
            System.out.println("중복된 아이디입니다.");
            message = "새로운 아이디로 가입을 시도하시겠습니까? Y/N";
            String yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                register();
            }
        }
    }

    */

    private void auth() {
        String message;
        message = "아이디를 입력해주세요.";
        String username = ScannerUtil.nextLine(SCANNER, message);

        message = "비밀번호를 입력해주세요.";
        String password = ScannerUtil.nextLine(SCANNER, message);

        StaffController staffController = new StaffController(connection);

        logIn = staffController.auth(username, password);

        if (logIn == null) {
            System.out.println("로그인 정보가 정확하지 않습니다.");
        }
    }

    private void showMenu() {
        String message = "1. 대여 2. 재고 관리 3. 회원 관리 4.내 정보 보기 5. 로그아웃";
        while (logIn != null) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message);
            if (userChoice == 1) {
                int customer_id = getCustomerId();
                if(customer_id!=0) {
                    RentalView rentalView = new RentalView(SCANNER, connection, logIn, customer_id);
                    rentalView.showMenu();
                }
            } else if (userChoice == 2) {
                InventoryView inventoryView = new InventoryView(SCANNER, connection, logIn);
                inventoryView.showMenu();
            } else if (userChoice == 3){
                CustomerView customerView = new CustomerView(SCANNER, connection, logIn);
                customerView.showMenu();
            } else if (userChoice == 4) {
                printOne();
            } else if (userChoice == 5) {
                logIn = null;
                System.out.println("정상적으로 로그아웃되었습니다.");
            }
        }
    }

    private void showAdmin() {
        String message = "1. 영화 정보 관리  2. 배우 정보 관리  3.내 정보 보기  4. 로그아웃";
        while (logIn != null) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message);
            if (userChoice == 1) {
                FilmView filmView = new FilmView(SCANNER, connection, logIn);
                filmView.showMenu();
            } else if (userChoice == 2) {
                ActorView actorView = new ActorView(SCANNER, connection, logIn);
                actorView.showMenu();
            } else if (userChoice == 3) {
                printOne();
            } else if (userChoice == 4) {
                logIn = null;
                System.out.println("정상적으로 로그아웃되었습니다.");
            }
        }
    }

    private int getCustomerId(){
        int customer_id = 0;
        ArrayList<CustomerDTO> list;
        boolean select = false;
        String message = "고객의 성을 입력해주세요.";
        String first_name = ScannerUtil.nextLine(SCANNER,message);

        message = "고객의 이름을 입력해주세요.";
        String last_name = ScannerUtil.nextLine(SCANNER,message);

        CustomerController customerController = new CustomerController(connection);

        list = customerController.selectList(first_name,last_name);

        if(list.isEmpty()){
            System.out.println("가입되지 않은 고객입니다.");
        }else {

            while (!select) {
                for (CustomerDTO i : list) {
                    System.out.println(i.getCustomer_id() + ". " + i.getEmail());
                }

                message = "대여 및 기록조회할 고객을 선택해주세요.";
                customer_id = ScannerUtil.nextInt(SCANNER, message);

                for (CustomerDTO i : list) {
                    if (customer_id == i.getCustomer_id()) {
                        select = true;
                    }
                }
            }
        }

        return customer_id;
    }

    private void printOne() {
        StaffController staffController = new StaffController(connection);

        System.out.println("이름 : " + logIn.getFirst_name() + " " + logIn.getLast_name());
        System.out.println("이메일 : " + logIn.getEmail());
        System.out.println("주소 : " + staffController.getAddress(logIn.getAddress_id()));
        System.out.println("---------------------------------------------");
        String message = "1. 주소 변경 2. 비밀번호 변경 3. 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        if (userChoice == 1) {
            updateAddress(staffController);
            printOne();
        } else if (userChoice == 2) {
            updatePassword(staffController);
            printOne();
        }
    }

    private void updateAddress(StaffController staffController) {
        ArrayList<AddressDTO> list;
        int address_id = 0;
        boolean select = false;
        String address;
        String message;

        message = "검색할 주소를 입력해주세요.(나라)";
        String country = ScannerUtil.nextLine(SCANNER, message);

        while (!staffController.getCountry(country)) {
            System.out.println("나라 이름을 제대로 입력해주세요.");
            message = "검색할 주소를 입력해주세요.(나라)";
            country = ScannerUtil.nextLine(SCANNER, message);
        }

        ArrayList<AddressDTO> cityList = staffController.getCityList(country);

        for (AddressDTO a : cityList) {
            System.out.println(a.getCity_id() + ". " + a.getCity());
        }

        message = "검색할 주소를 입력해주세요.(도시)";
        String city = ScannerUtil.nextLine(SCANNER, message);

        while (!staffController.getCity(city)) {
            System.out.println("도시 이름을 제대로 입력해주세요.");
            for (AddressDTO a : cityList) {
                System.out.println(a.getCity_id() + ". " + a.getCity());
            }
            message = "검색할 주소를 입력해주세요.(도시)";
            city = ScannerUtil.nextLine(SCANNER, message);
        }

        ArrayList<AddressDTO> addressList = staffController.getAddressList(city);

        while (!select) {
            for (AddressDTO a : addressList) {
                System.out.println(a.getAddress_id() + ". " + a.getAddress());
            }

            message = "주소를 선택해주세요.";
            address_id = ScannerUtil.nextInt(SCANNER, message);

            for (AddressDTO a : addressList) {
                if (address_id == a.getAddress_id()) {
                    select = true;
                }
            }
        }

        logIn.setAddress_id(address_id);
        staffController.updateAddress(logIn.getStaff_id(), address_id);

    }

    private void updatePassword(StaffController staffController) {

        String message = "새로운 비밀번호를 입력해주세요.";
        String newPassword = ScannerUtil.nextLine(SCANNER, message);

        message = "기존 비밀번호를 입력해주세요.";
        String oldPassword = ScannerUtil.nextLine(SCANNER, message);

        if (logIn.getPassword().equals(oldPassword)) {
            logIn.setPassword(newPassword);
            staffController.updatePassword(logIn.getStaff_id(), newPassword);
        } else {
            System.out.println("회원 정보 변경에 실패하였습니다.");
        }
    }

    /*
    private void delete(StaffController staffController) {
        String message = "정말로 삭제하시겠습니까? Y/N";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            message = "비밀번호를 입력해주세요.";
            String password = ScannerUtil.nextLine(SCANNER, message);

            if (staffController.auth(logIn.getUsername(), password) != null) {
                staffController.delete(logIn.getStaff_id());
                logIn = null;
            }
        }
    }

     */
}
