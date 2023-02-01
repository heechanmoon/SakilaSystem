package View;

import Controller.CustomerController;
import Controller.StaffController;
import Model.AddressDTO;
import Model.CustomerDTO;
import Model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerView {
    private final Scanner SCANNER;
    private Connection connection;
    private StaffDTO logIn;
    private CustomerController customerController;

    public CustomerView(Scanner scanner, Connection connection, StaffDTO staffDTO) {
        this.SCANNER = scanner;
        this.connection = connection;
        this.logIn = staffDTO;
        customerController = new CustomerController(this.connection);
    }

    public void showMenu() {
        String message = "1. 회원 추가 2. 회원 조회 3. 종료";

        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message,1,3);
            if (userChoice == 1) {
                insert();
            } else if (userChoice == 2) {
                int customer_id = getCustomerId();
                if(customer_id!=0){
                    printOne(customer_id);
                }
            } else if(userChoice == 3){
                break;
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

                message = "정보를 조회할 고객을 선택해주세요.";
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


    private void insert(){
        StaffController staffController = new StaffController(connection);
        int address_id = 0;
        boolean select = false;
        CustomerDTO customerDTO = new CustomerDTO();
        String message = "고객의 성을 입력해주세요.";
        customerDTO.setFirst_name(ScannerUtil.nextLine(SCANNER,message));

        message = "고객의 이름을 입력해주세요.";
        customerDTO.setLast_name(ScannerUtil.nextLine(SCANNER,message));

        message = "이메일을 입력해주세요.";
        customerDTO.setEmail(ScannerUtil.nextLine(SCANNER, message));

        while(customerController.duplicateEmail(customerDTO.getEmail())){
            message = "중복된 이메일 주소입니다. 다른 이메일을 입력해주세요.";
            customerDTO.setEmail(ScannerUtil.nextLine(SCANNER, message));
        }

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

        customerDTO.setAddress_id(address_id);

        CustomerController customerController = new CustomerController(connection);

        customerController.insert(customerDTO);
    }

    private void printOne(int customer_id) {
        CustomerDTO select;
        CustomerController customerController = new CustomerController(connection);
        select = customerController.selectOne(customer_id);

        System.out.println("이름 : " + select.getFirst_name() + " " + select.getLast_name());
        System.out.println("이메일 : " + select.getEmail());
        System.out.println("주소 : " + customerController.getAddress(select.getAddress_id()));
        System.out.println("---------------------------------------------");
        String message = "1. 주소 변경 2. 탈퇴 3. 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        if (userChoice == 1) {
            updateAddress(customerController, select);
            printOne(customer_id);
        } else if (userChoice == 2) {
            delete(customerController, select);
        } else if (userChoice == 3) {
        }
    }

    public void updateAddress(CustomerController customerController, CustomerDTO selectDTO) {
        StaffController staffController = new StaffController(connection);
        int address_id = 0;
        boolean select = false;
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

        selectDTO.setAddress_id(address_id);
        customerController.updateAddress(selectDTO);
    }

    private void delete(CustomerController customerController, CustomerDTO customerDTO) {
        String message = "정말로 삭제하시겠습니까? Y/N";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            customerController.delete(customerDTO);
        }
    }
}
