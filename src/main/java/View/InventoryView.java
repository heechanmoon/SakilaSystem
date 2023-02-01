package View;

import Controller.InventoryController;
import Controller.FilmController;
import Model.FilmDTO;
import Model.StaffDTO;
import Model.InventoryDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryView {
    private InventoryController inventoryController;
    private FilmController filmController;
    private final Scanner SCANNER;
    private Connection connection;
    private StaffDTO logIn;

    public InventoryView(Scanner scanner, Connection connection, StaffDTO staffDTO) {
        this.SCANNER = scanner;
        this.connection = connection;
        this.logIn = staffDTO;
        inventoryController = new InventoryController(this.connection);
        filmController = new FilmController(this.connection);
    }

    public void showMenu() {
        String message = "1. 비디오 검색  2. 비디오 매입  3. 비디오 폐기  4.종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message,1,4);
            if (userChoice == 1) {
                findFilm();
            } else if (userChoice == 2) {
                buyFilm();
            } else if (userChoice == 3) {
                destroyFilm();
            } else if (userChoice == 4) {
                break;
            }
        }
    }

    public void findFilm(){
        ArrayList<InventoryDTO> list;
        String title;

        String message = "검색할 비디오의 제목을 입력해주세요.";
        title = ScannerUtil.nextLine(SCANNER, message);

        list = inventoryController.selectList(title, logIn.getStaff_id());

        for(InventoryDTO i : list){
            System.out.println(i.getInventory_id()+". "+title);
        }
    }

    public void buyFilm(){
        InventoryDTO inventory = new InventoryDTO();
        String title;
        ArrayList<FilmDTO> list;
        int film_id = 0;
        boolean select = false;
        String message = "비디오 제목 : ";
        title = ScannerUtil.nextLine(SCANNER, message);

        list = filmController.selectList(title);

        while(list.isEmpty()) {
            message = "해당 제목의 비디오가 존재하지 않습니다. 다시 입력해주세요.";
            title = ScannerUtil.nextLine(SCANNER, message);

            list = filmController.selectList(title);
        }

        while(!select) {
            for (FilmDTO i : list) {
                System.out.println(i.getFilm_id() + ". " + i.getTitle());
            }

            message = "매입할 비디오를 선택해주세요.";
            film_id = ScannerUtil.nextInt(SCANNER, message);

            for (FilmDTO i : list) {
                if (film_id == i.getFilm_id()) {
                    select = true;
                }
            }
        }

        inventory.setFilm_id(film_id);
        inventory.setStore_id(logIn.getStore_id());

        inventoryController.insert(inventory);
    }

    public void destroyFilm(){
        String title;
        ArrayList<InventoryDTO> list;
        boolean select = false;
        int inventory_id = 0;
        String message = "폐기할 비디오 제목 : ";
        title = ScannerUtil.nextLine(SCANNER, message);

        list = inventoryController.selectList(title, logIn.getStaff_id());

        while(list.isEmpty()) {
            message = "해당 제목의 비디오가 존재하지 않습니다. 다시 입력해주세요.";
            title = ScannerUtil.nextLine(SCANNER, message);

            list = inventoryController.selectList(title, logIn.getStaff_id());
        }

        while(!select) {
            for (InventoryDTO i : list) {
                System.out.println(i.getInventory_id() + ". " + title);
            }

            message = "폐기할 비디오를 선택해주세요.";
            inventory_id = ScannerUtil.nextInt(SCANNER, message);

            for (InventoryDTO i : list) {
                if (inventory_id == i.getInventory_id()) {
                    select = true;
                }
            }
        }

        inventoryController.delete(inventory_id);
    }
}
