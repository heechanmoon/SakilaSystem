package View;

import Controller.ActorController;
import Controller.CustomerController;
import Controller.FilmController;
import Model.ActorDTO;
import Model.FilmDTO;
import Model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class FilmView {
    private final Scanner SCANNER;
    private final int RELEASE_YEAR = 2023;
    private Connection connection;
    private StaffDTO logIn;
    private FilmController filmController;
    private ActorController actorController;

    public FilmView(Scanner scanner, Connection connection, StaffDTO staffDTO) {
        this.SCANNER = scanner;
        this.connection = connection;
        this.logIn = staffDTO;
        filmController = new FilmController(this.connection);
        actorController = new ActorController(this.connection);
    }

    public void showMenu() {
        String message = "1.비디오 추가  2.비디오 목록  3.종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message,1,4);
            if (userChoice == 1) {
                insert();
            } else if (userChoice == 2) {
                showFilm();
            } else if (userChoice == 3) {
                break;
            }
        }
    }

    public void insert(){
        FilmDTO filmDTO = new FilmDTO();
        String specialFeatures = "";
        int rate;
        int actors = 0;
        boolean duplicateActor = false;
        boolean commaOn = false;
        boolean select = false;
        ArrayList<ActorDTO> actorList = new ArrayList<>();
        String message = "영화 제목 입력 : ";
        filmDTO.setTitle(ScannerUtil.nextLine(SCANNER, message));

        message = "영화 줄거리 입력 : ";
        filmDTO.setDescription(ScannerUtil.nextLine(SCANNER, message));

        message = "영화 등급 : (1.'G', 2.'PG', 3.'PG-13', 4.'R', 5.'NC-17')";
        rate = ScannerUtil.nextInt(SCANNER, message, 1, 5);
        if(rate==1){
            filmDTO.setRating("G");
        } else if (rate==2) {
            filmDTO.setRating("PG");
        } else if (rate==3) {
            filmDTO.setRating("PG-13");
        } else if (rate==4) {
            filmDTO.setRating("R");
        } else if (rate==5) {
            filmDTO.setRating("NC-17");
        }

        //SET('Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes')

        message = "Special features - Trailers (y/n)";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        while(!(yesNo.equalsIgnoreCase("Y") || yesNo.equalsIgnoreCase("N"))){
            message = "Special features - Trailers (y/n)";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
        }

        if (yesNo.equalsIgnoreCase("Y")) {
            specialFeatures = specialFeatures + "Trailers";
            commaOn = true;
        }else{
            commaOn = false;
        }

        message = "Special features - Commentaries (y/n)";
        yesNo = ScannerUtil.nextLine(SCANNER, message);

        while(!(yesNo.equalsIgnoreCase("Y") || yesNo.equalsIgnoreCase("N"))){
            message = "Special features - Commentaries (y/n)";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
        }

        if (yesNo.equalsIgnoreCase("Y")) {
            if(commaOn) {
                specialFeatures = specialFeatures + ",Commentaries";
            }else{
                specialFeatures = specialFeatures + "Commentaries";
                commaOn = true;
            }
        }

        message = "Special features - Deleted Scenes (y/n)";
        yesNo = ScannerUtil.nextLine(SCANNER, message);

        while(!(yesNo.equalsIgnoreCase("Y") || yesNo.equalsIgnoreCase("N"))){
            message = "Special features - Deleted Scenes (y/n)";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
        }

        if (yesNo.equalsIgnoreCase("Y")) {
            if(commaOn) {
                specialFeatures = specialFeatures + ",Deleted Scenes";
            }else{
                specialFeatures = specialFeatures + "Deleted Scenes";
                commaOn = true;
            }
        }

        message = "Special features - Behind the Scenes (y/n)";
        yesNo = ScannerUtil.nextLine(SCANNER, message);

        while(!(yesNo.equalsIgnoreCase("Y") || yesNo.equalsIgnoreCase("N"))){
            message = "Special features - Behind the Scenes (y/n)";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
        }

        if (yesNo.equalsIgnoreCase("Y")) {
            if(commaOn) {
                specialFeatures = specialFeatures + ",Behind the Scenes ";
            }else{
                specialFeatures = specialFeatures + "Behind the Scenes ";
            }
        }

        filmDTO.setSpecial_features(specialFeatures);

        message = "총 출연 배우수 : ";
        actors = ScannerUtil.nextInt(SCANNER,message,1,30);

        for(int i=0; i<actors; i++){
            ArrayList<ActorDTO> selectActor;
            message = "출연한 배우의 정보를 입력하세요.\n성 : ";
            ActorDTO actor = new ActorDTO();
            actor.setFirst_name(ScannerUtil.nextLine(SCANNER,message));
            message = "이름 : ";
            actor.setLast_name(ScannerUtil.nextLine(SCANNER,message));

            boolean isActor = actorController.getIsActor(actor);

            if(!actorList.isEmpty()) {
                for (ActorDTO a : actorList) {
                    if (a.getFirst_name().equals(actor.getFirst_name()) && a.getLast_name().equals(actor.getLast_name())) {
                        duplicateActor = true;
                    }
                }
            }

            if(duplicateActor || isActor){
                System.out.println("이미 등록된 배우거나 배우 목록에 없습니다.");
                i--;
            }else{
                int selectOne = 0;
                selectActor = actorController.selectList(actor.getFirst_name(), actor.getLast_name());
                for(ActorDTO a : selectActor){
                    System.out.println(a.getActor_id()+". "+a.getFirst_name()+" "+a.getLast_name());
                }
                
                while(!select){
                    message = "등록할 배우를 선택하세요.";

                    selectOne = ScannerUtil.nextInt(SCANNER, message);
                    
                    for(ActorDTO a : selectActor){
                        if(a.getActor_id()==selectOne){
                            select = true;
                        }
                    }
                }
                
                actor.setActor_id(selectOne);
                
                actorList.add(actor);

                select = false;
            }

            duplicateActor = false;
        }


        message = "카테고리 목록\n" +
                "1. Action\n" +
                "2. Animation\n" +
                "3. Children\n" +
                "4. Classics\n" +
                "5. Comedy\n" +
                "6. Documentary\n" +
                "7. Drama\n" +
                "8. Family\n" +
                "9. Foreign\n" +
                "10. Games\n" +
                "11. Horror\n" +
                "12. Music\n" +
                "13. New\n" +
                "14. Sci-Fi\n" +
                "15. Sports\n" +
                "16. Travel";

        filmDTO.setCategory_id(ScannerUtil.nextInt(SCANNER,message,1,16));

        message = "언어 : (1. 영어 2. 이탈리아어 3. 일본어 4. 만다린 5. 프랑스어 6. 독일어)";
        filmDTO.setLanguage_id(ScannerUtil.nextInt(SCANNER,message,1,6));

        filmDTO.setRelease_year(RELEASE_YEAR);

        message = "비디오 길이(분) : ";
        filmDTO.setLength(ScannerUtil.nextInt(SCANNER,message,3,1440));

        message = "비디오 대여가격(달러) : ";
        filmDTO.setRental_rate(ScannerUtil.nextBigDecimal(SCANNER,message));

        message = "비디오 대여기간(3일~7일) : ";
        filmDTO.setRental_duration(ScannerUtil.nextInt(SCANNER,message,3,7));

        message = "비디오 교체 비용 : ";
        filmDTO.setReplacement_cost(ScannerUtil.nextBigDecimal(SCANNER,message));

        filmController.insert(filmDTO,actorList);
    }

    public void showFilm() {
        boolean select = false;
        int number = -1;

        String message = "비디오 제목 : ";
        String title = ScannerUtil.nextLine(SCANNER, message);

        ArrayList<FilmDTO> filmList = filmController.selectList(title);

        for (FilmDTO f : filmList) {
            System.out.println(f.getFilm_id() + ". " + f.getTitle());
        }

        while (!select && number != 0) {
            message = "상세보기할 비디오를 선택해주세요. (뒤로 가기 : 0)";
            number = ScannerUtil.nextInt(SCANNER, message);
            for (FilmDTO f : filmList) {
                if (f.getFilm_id() == number) {
                    select = true;
                }
            }

            if(number == 0){
                select = true;
            }
        }

        if (number != 0) {
            FilmDTO film = filmController.selectOne(number);
            printOne(film);
        }
    }

    public void printOne(FilmDTO film){
        System.out.println("번호 : " + film.getFilm_id());
        System.out.println("제목 : " + film.getTitle());
        System.out.println("줄거리 : " + film.getDescription());
        System.out.println("개봉연도 : " + film.getRelease_year());
        System.out.println("장르 : " + filmController.getCategory(film.getCategory_id()));
        System.out.println("등급 : " + film.getRating());
        System.out.println("Special features : " + film.getSpecial_features());
        System.out.println("Language : " + filmController.getLanguageString(film.getLanguage_id()));
        System.out.println("비디오 길이 : " + film.getLength() + "분");
        System.out.println("등장 배우 : ");
        ArrayList<ActorDTO> actorList = filmController.actorList(film.getFilm_id());
        for(ActorDTO a : actorList){
            System.out.println(a.getActor_id() + ". " + a.getFirst_name()+" "+a.getLast_name());
        }
        System.out.println("대여기간 : " + film.getRental_duration() + "일");
        System.out.println("대여료 : " + film.getRental_rate() + "$");
        System.out.println("교체비용 : " + film.getReplacement_cost() + "$");
        System.out.println("---------------------------------------------");
        String message = "1. 정보 변경  2. 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        if (userChoice == 1) {
            update(film);
            printOne(film);
        }
    }

    public void update(FilmDTO film){
        String message = "수정할 비디오 대여가격(달러) : ";
        film.setRental_rate(ScannerUtil.nextBigDecimal(SCANNER,message));

        message = "수정할 비디오 대여기간(3일~7일) : ";
        film.setRental_duration(ScannerUtil.nextInt(SCANNER,message,3,7));

        message = "수정할 비디오 교체 비용 : ";
        film.setReplacement_cost(ScannerUtil.nextBigDecimal(SCANNER,message));

        filmController.update(film);
    }
}
