package collection;

import java.util.HashMap;

public class CollectionsManager {
//    public LinkedList<Movie> updateInititalCollection(){
//        // на вход поступает коллекция, как-то изменяем(добавляем элемент, например), а потом передаем в LinkedCollection
//    }
    private final HashMap<String, LinkedCollection> controller;

    public CollectionsManager(){
        this.controller = new HashMap<>();
    }
//
//    public LinkedList<Movie> getCollection(){
//        return controller;
//    }
//
    /*todo здесь должна быть HashMap, ключом которой является имя пользователя, а значением - его коллекция.
        ну вот например, имя пользователя - какой-то уникальный логин
        если такой логин, который мы вводим уже имеется, то мы предупреждаем пользователя, что такой логин создать нельзя
        иначе мы добавляем в HashMap ключ - логин пользователя и его коллекцию
     */



}
//здесь хранятся коллекции множества людей
//то есть я должен вводить имя пользователя, искать его коллекцию в CollectionManager, а дальше ее как то обрабатывать

