package com.example.greenfarm;

import android.util.Log;

import com.example.greenfarm.pojo.User;
import com.google.gson.Gson;

import org.junit.Test;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void GsonTest() {
        Gson gson = new Gson();
        User user = new User("admin","12345","13617211801");
        String json = gson.toJson(user);
        System.out.println(json);
    }


}