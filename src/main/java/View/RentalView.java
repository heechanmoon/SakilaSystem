package View;

import Controller.FilmController;
import Controller.InventoryController;
import Controller.RentalController;
import Model.FilmDTO;
import Model.InventoryDTO;
import Model.StaffDTO;
import Model.RentalDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class RentalView {
    private InventoryController inventoryController;
    private RentalController rentalController;
    private FilmController filmController;
    private final Scanner SCANNER;
    private Connection connection;
    private StaffDTO logIn;
    private int customerId;

    public RentalView(Scanner scanner, Connection connection, StaffDTO staffDTO, int customerId) {
        this.SCANNER = scanner;
        this.connection = connection;
        this.logIn = staffDTO;
        this.customerId = customerId;
        inventoryController = new InventoryController(this.connection);
        rentalController = new RentalController(this.connection);
        filmController = new FilmController(this.connection);
    }

    public void showMenu() {
        String message = "1. 대여  2. 반납  3. 종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message,1,3);
            if (userChoice == 1) {
                rentFilm();
            } else if (userChoice == 2) {
                returnFilm();
            } else if (userChoice == 3) {
                break;
            }
        }
    }

    public void rentFilm(){
        RentalDTO rentalDTO = new RentalDTO();
        String title;
        ArrayList<InventoryDTO> list;
        int inventory_id = 0;
        boolean select = false;
        String message = "비디오 제목 : ";
        title = ScannerUtil.nextLine(SCANNER, message);

        list = inventoryController.selectList(title,logIn.getStaff_id());

        if(list.isEmpty()) {
            System.out.println("해당 제목의 비디오가 존재하지 않아서 대여를 할 수 없습니다");
        }else {

            while (!select) {
                for (InventoryDTO i : list) {
                    System.out.println(i.getInventory_id() + ". " + title);
                }

                message = "대여할 비디오를 선택해주세요.";
                inventory_id = ScannerUtil.nextInt(SCANNER, message);

                for (InventoryDTO i : list) {
                    if (inventory_id == i.getInventory_id()) {
                        select = true;
                    }
                }
            }

            rentalDTO.setStaff_id(logIn.getStaff_id());
            rentalDTO.setInventory_id(inventory_id);
            rentalDTO.setCustomer_id(customerId);

            rentalController.insert(rentalDTO);
        }
    }

    public void returnFilm(){

        ArrayList<RentalDTO> list = notReturn();
        int returnRental = 0;

        if(list.isEmpty()){
            System.out.println("미반납한 비디오가 없습니다.");
        }else {
            boolean select = false;

            while(!select) {
                String message = "반납처리할 대여 번호를 입력해주세요.";
                returnRental = ScannerUtil.nextInt(SCANNER, message);

                for(RentalDTO r : list){
                    if(returnRental == r.getRental_id()){
                        select = true;
                    }
                }
            }

            rentalController.returnFilm(returnRental);
        }
    }

    public ArrayList<RentalDTO> notReturn(){
        ArrayList<RentalDTO> list;
        InventoryDTO returnFilm;
        FilmDTO getFilm;

        list = rentalController.selectList(customerId, logIn.getStaff_id());

        for (RentalDTO i : list) {
            returnFilm = inventoryController.selectOne(i.getInventory_id());
            getFilm = filmController.selectOne(returnFilm.getFilm_id());

            System.out.println(i.getRental_id() + ". " + i.getRental_date() + " : " + i.getInventory_id() + " 번 비디오 " + getFilm.getTitle() + " 대여");
        }

        return list;
    }
}
