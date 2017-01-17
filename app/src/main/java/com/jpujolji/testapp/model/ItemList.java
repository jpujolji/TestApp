package com.jpujolji.testapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemList extends RealmObject {

    @PrimaryKey
    public String id;

    public String title, header_img, icon_img, description, public_description, header_title;

}
