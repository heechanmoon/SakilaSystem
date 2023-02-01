package View;

import Controller.ActorController;
import Model.ActorDTO;
import Model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ActorView {
    private final Scanner SCANNER;
    private Connection connection;
    private StaffDTO logIn;
    private ActorController actorController;

    public ActorView(Scanner scanner, Connection connection, StaffDTO staffDTO) {
        this.SCANNER = scanner;
        this.connection = connection;
        this.logIn = staffDTO;
        actorController = new ActorController(this.connection);
    }

    public void showMenu() {
        String message = "1.배우 정보 추가  2.배우 정보 삭제  3. 배우 정보 조회  4.종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message,1,4);
            if (userChoice == 1) {
                insert();
            } else if (userChoice == 2) {
                delete();
            } else if (userChoice == 3) {
                actorList();
            } else if (userChoice == 4) {
                break;
            }
        }
    }

    public void insert(){
        System.out.println("추가할 배우의 정보를 입력해주세요. ");
        ActorDTO actorDTO = new ActorDTO();
        String message = "성 : ";
        actorDTO.setFirst_name(ScannerUtil.nextLine(SCANNER,message));

        message = "이름 : ";
        actorDTO.setLast_name(ScannerUtil.nextLine(SCANNER,message));

        actorController.insert(actorDTO);
    }

    public void delete(){
        System.out.println("삭제할 배우의 정보를 입력해주세요. ");
        ArrayList<ActorDTO> list;
        String message = "성 : ";
        String first_name = ScannerUtil.nextLine(SCANNER,message);

        message = "이름 : ";
        String last_name = ScannerUtil.nextLine(SCANNER,message);

        list = actorController.selectList(first_name, last_name);

        if(list.isEmpty()){
            System.out.println("해당 배우는 존재하지 않습니다.");
        }else{
            for(ActorDTO a : list){
                System.out.println(a.getActor_id()+". "+a.getFirst_name()+" "+a.getLast_name());
            }

            message = "삭제할 배우를 선택해주세요.";
            int deleteId = ScannerUtil.nextInt(SCANNER,message);

            boolean isDelete = actorController.delete(deleteId);

            if(!isDelete){
                System.out.println("이미 출연한 작품이 있어서 삭제가 불가능합니다.");
            }
        }
    }

    public void actorList(){
        System.out.println("조회할 배우의 정보를 입력해주세요. ");
        ArrayList<ActorDTO> list;
        ActorDTO actorDTO = null;
        int selectId = 0;
        boolean select = false;
        String message = "성 : ";
        String first_name = ScannerUtil.nextLine(SCANNER,message);

        message = "이름 : ";
        String last_name = ScannerUtil.nextLine(SCANNER,message);

        list = actorController.selectList(first_name, last_name);

        if(list.isEmpty()){
            System.out.println("해당 배우는 존재하지 않습니다.");
        }else{

            while(!select) {
                for (ActorDTO a : list) {
                    System.out.println(a.getActor_id() + ". " + a.getFirst_name() + " " + a.getLast_name());
                }

                message = "조회할 배우를 선택해주세요.";
                selectId = ScannerUtil.nextInt(SCANNER, message);

                actorDTO = actorController.selectOne(selectId);

                for(ActorDTO a : list){
                    if(actorDTO != null && a.getFirst_name().equals(actorDTO.getFirst_name()) && a.getLast_name().equals(actorDTO.getLast_name())){
                        select = true;
                    }
                }
            }

            show(actorDTO);
        }
    }

    public void show(ActorDTO actorDTO){
        System.out.println("이름 : "+actorDTO.getLast_name()+" "+actorDTO.getFirst_name());
        ArrayList<String> filmList;
        filmList = actorController.actorFilmList(actorDTO.getActor_id());

        System.out.println("출연 작품 목록");
        for(String title : filmList){
            System.out.println(title);
        }
    }
}
