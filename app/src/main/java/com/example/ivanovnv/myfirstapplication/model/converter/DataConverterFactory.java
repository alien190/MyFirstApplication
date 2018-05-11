package com.example.ivanovnv.myfirstapplication.model.converter;

import android.support.annotation.Nullable;

import com.example.ivanovnv.myfirstapplication.comments.Comment;
import com.example.ivanovnv.myfirstapplication.comments.CommentId;
import com.example.ivanovnv.myfirstapplication.model.Data;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class DataConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {


        if (!(type == CommentId.class)) {
            Type envelopedType = TypeToken.getParameterized(Data.class, type).getType();
            final Converter<ResponseBody, Data> delegate = retrofit.nextResponseBodyConverter(this, envelopedType, annotations);

            return body -> {
                Data<?> data = delegate.convert(body);
                return data.response;
            };
        } else {
            return null;
        }
    }
}