package entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.ZonedDateTime birthday; //Поле может быть null
    private float weight; //Значение поля должно быть больше 0
    private Country nationality; //Поле может быть null

    public Person(String name, java.time.ZonedDateTime birthday, float weight, Country nationality){
        this.name = name;
        this.birthday = birthday;
        this.weight = weight;
        this.nationality = nationality;

    }

    public String getPersonsName(){
        return name;

    }
    public ZonedDateTime getBirthday(){
        return birthday;
    }

    public float getWeight(){
        return weight;
    }

    public Country getNationality(){
        return nationality;
    }

    public String toString(){
        return getPersonsName() + "," + getBirthday().toString() + "," + getWeight() + "," + getNationality();
    }
}
