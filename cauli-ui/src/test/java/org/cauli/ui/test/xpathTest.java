package org.cauli.ui.test;


import com.alibaba.fastjson.JSON;
import org.cauli.junit.JUnitBaseRunner;
import org.cauli.junit.anno.Bean;
import org.cauli.junit.anno.Param;
import org.cauli.junit.anno.ThreadRunner;
import org.cauli.ui.runner.CauliUIRunner;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Created by tianqing.wang on 14-3-31
 */
@RunWith(JUnitBaseRunner.class)
//@Retry(1)
//@Filter
//@ThreadRunner(threads = 2)
//@Require(Engine.FIREFOX)
//@Ignore
public class xpathTest {


    //@Test
    @Param("test.xls")
    @Ignore
    public void xlsTest(@Bean("user")UserDto userDto){
        if(userDto.getName()==null){
            System.out.println("null!!!!");
        }else{
            System.out.println(userDto.getName()+"->"+userDto.getAge());
        }

    }


    @Test
    @Param("test.xls")
    public void test(@Bean("user")UserDto userDto){
        System.out.println(JSON.toJSONString(userDto,true));
    }
}
