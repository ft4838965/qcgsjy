package com.stylefeng.guns.system;

import com.stylefeng.guns.util.AdressUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Heyifan Cotter on 2019/3/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MapTest {

    @Test
    public void mapTest(){
        String city = AdressUtil.getCity("106.63", "26.65");
        System.out.println(city);
    }
}
