package entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

public class Movie implements Comparable<Movie>, Serializable {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer oscarsCount; //Значение поля должно быть больше 0, Поле может быть null
    private Long length; //Поле не может быть null, Значение поля должно быть больше 0
    private MovieGenre genre; //Поле может быть null
    private MpaaRating mpaaRating; //Поле не может быть null
    private Person operator; //Поле не может быть null

    public Movie(long id,
                 String name,
                 Coordinates coordinates,
                 java.util.Date creationDate,
                 Integer oscarsCount,
                 Long length,
                 MovieGenre genre,
                 MpaaRating mpaaRating,
                 Person operator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.length = length;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    public long getId(){
        return id;
    }

    public String getMovieName(){
        return name;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public Integer getOscarsCount(){
        return oscarsCount;
    }

    public Long getLength(){
        return length;
    }

    public MovieGenre getGenre(){
        return genre;
    }

    public MpaaRating getMpaaRating(){
        return mpaaRating;
    }

    public Person getOperator(){
        return operator;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setOscarsCount(Integer oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    //    public String toString(){
//        return getId() + ", " +
//                getMovieName() + ", " +
//                getCoordinates() + ", " +
//                getCreationDate() + ", " +
//                getOscarsCount() + ", " +
//                getLength()+ ", " +
//                getGenre() + ", " +
//                getMpaaRating()+ ", " +
//                getOperator();
//    }
    public String toString(){
        return getMovieName();
    }
    public String allInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(",")
                    .append(getMovieName()).append(",")
                    .append(getCoordinates()).append(",")
                    .append(getCreationDate()).append(",")
                    .append(getOscarsCount()).append(",")
                    .append(getLength()).append(",")
                    .append(getGenre()).append(",")
                    .append(getMpaaRating()).append(",")
                    .append(getOperator());
        sb.append("\n");
        return sb.toString();
    }



        public static Movie Default = new Movie(
            1,
            "Name",
            new Coordinates(1, 1),
            new Date(),
            1,
            12L,
            MovieGenre.ACTION,
            MpaaRating.G,
            new Person("Oleg", ZonedDateTime.now(), 1.5F, Country.INDIA)

    );

    public void setCoordinates(int parseX, int parseY) {
        this.coordinates = new Coordinates(parseX, parseY);
    }

    @Override
    public int compareTo(Movie o) {
        return this.getOscarsCount() - o.getOscarsCount();
    }
}

