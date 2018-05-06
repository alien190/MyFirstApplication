package com.example.ivanovnv.myfirstapplication.db;

import android.arch.persistence.room.TypeConverter;

import com.example.ivanovnv.myfirstapplication.model.Album;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AlbumConverter {

//    @TypeConverter
//    public static List<Integer> fromAlbum(List<Album> albums) {
//        Collection<Integer> ids = CollectionUtils.
//                collect(albums, TransformerUtils.invokerTransformer("getId"));
//        return new ArrayList<Integer>(){{addAll(ids);}};
//    }

    @TypeConverter
    public static String fromAlbums(List<Album> albums) {
        return "";
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        if (date==null) {
            return(null);
        }

        return(date.getTime());
    }
}

