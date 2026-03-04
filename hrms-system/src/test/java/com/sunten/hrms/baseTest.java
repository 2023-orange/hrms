package com.sunten.hrms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.util.AcUtil;
import com.sunten.hrms.ac.vo.AcRegimeTimeVo;
import com.sunten.hrms.domain.DomainEqualsResult;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.utils.DomainEqualsUtil;
import com.sunten.hrms.utils.EncryptUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class baseTest {
    class MapJ {
        private String key;//替换的编号
        private String value;//值

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public MapJ(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }

        public MapJ() {
            super();
        }

    }

    @Test
    public void testGenerator() {
        Timestamp aa = Timestamp.valueOf("2018-12-04 00:00:00.0");
        System.out.println(aa);
    }

    @Test
    public void testJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalDate date = LocalDate.of(2018, 5, 5);
        String dateStr = mapper.writeValueAsString(date);
        Assert.assertEquals("\"2018-05-05\"", dateStr);

        LocalDateTime dateTime = LocalDateTime.of(2018, 5, 5, 1, 1, 1);
        Assert.assertEquals("\"2018-05-05T01:01:01\"", mapper.writeValueAsString(dateTime));
    }

    class JavaScript {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        public Double getMathValue(List<MapJ> map, String option) {
            double d = 0;
            try {
                for (int i = 0; i < map.size(); i++) {
                    MapJ mapj = map.get(i);
                    System.out.println(mapj.getKey() + "=" + mapj.getValue());
                    option = option.replaceAll(mapj.getKey(), mapj.getValue());
                }
                System.out.println(option);
                Object o = engine.eval(option);
                d = Double.parseDouble(o.toString());
            } catch (ScriptException e) {
                System.out.println("无法识别表达式");
                return null;
            }
            return d;
        }
    }


    @Test
    public void testJavaScript() {
        String sbt = "(<XINDIAN>*0.4+<XINDIAN>*0.6*<SCXS>*<ZLXS>*<GRXS>)+<XINDIAN1>";
        List<MapJ> all = new ArrayList<MapJ>();
        all.add(new MapJ("<XINDIAN>", "8000"));
        all.add(new MapJ("<XINDIAN1>", "1000"));
        all.add(new MapJ("<SCXS>", "0.9"));
        all.add(new MapJ("<ZLXS>", "1"));
        all.add(new MapJ("<GRXS>", "1.1"));
        JavaScript js = new JavaScript();
        Double d = js.getMathValue(all, sbt);
        if (d == null) {
            System.out.println("                 无法计算这个表达式");
        } else {
            System.out.println(d);
        }
    }

    @Test
    public void testEquals() {
        String a;
        String equalsCheckS = "id,jobName,enabledFlag,sequence,authorizedStrength,jobCode,jobClass,jobDescribes,dataScope,deletedFlag";
        FndJob domainOne = new FndJob();
        domainOne.setId((long) 1);
        domainOne.setJobName("job1");
        domainOne.setJobCode("job1Code");
        domainOne.setCreateBy((long) -1);
        FndJob domainTwo = new FndJob();
        domainTwo.setId((long) 2);
        domainTwo.setJobName("job2");
        domainTwo.setJobCode("job2Code");
        domainTwo.setCreateBy((long) -2);
        List<DomainEqualsResult> domainEqualsResults = DomainEqualsUtil.domainEquals(equalsCheckS, domainOne, domainTwo);
        for (DomainEqualsResult domainEqualsResult : domainEqualsResults) {
            System.out.println(domainEqualsResult);
        }
        System.out.println("123".substring("123".length() - 4));
    }


    @Test
    public void testLong() {
        Long id = -1L;

        System.out.println(id.equals(-1L));

    }

    @Test
    public void testCompanyAge() {
        Period period = Period.between(LocalDate.parse("2020-01-31"), LocalDate.parse("2020-03-01"));
        System.out.println(period.getYears() * 12 + period.getMonths());
        Set<Long> deptIds = new HashSet<>();
        deptIds.add(-1L);
        deptIds.add(1L);
        deptIds.add(-1L);
        deptIds.add(-1L);
        deptIds.add(-1L);
        deptIds.add(-1L);
        deptIds.add(-2L);
        deptIds.remove(-1L);
        System.out.println("deptIds.size:" + deptIds.size());
        deptIds.forEach(id -> {
            System.out.println(id);
        });
    }

    @Test
    public void pseudocodeTest() throws UnknownHostException {
        /**
         * 一、获取公司所有人员的排班情况数据（筛选请假信息后），employeeRegimeList
         *  List<EmployeeRegime> employeeRegimeList =  employeeRegimeService.getEmployeeRegimeList()
         * 二、获取所有人的打卡记录
         * List<signIn> allEmpSignInList = signInDao.getSignInList()
         * 三、循环排班情况和打卡记录，获取每个人员对应的打卡记录
         *  employeeRegimeList.forEach(emp => {
         *
         *      allEmpSignInList.forEach(signIn => {
         *          if (emp.getEmployeeId === signIn.getEmployeeId) {
         *              emp.add(signIn)
         *          }
         *      })
         *
         *  })
         *  第四步，循环员工及其下的打卡记录，判断其是否存在打卡异常，如存在，则生成异常记录
         * 0  employeeRegimeList.fotEach( emp => {
         * 1       if (未排班) {
         * 2         return '未排班异常'
         * 3      } else  {
         * 4          if (休息日 && 有打卡记录) return '休息日打卡异常';
         * 5
         * 6         else if (工作日 && 打卡记录为0) return '全天未打卡异常';
         * 7
         * 8          else if (工作日 && 打卡记录在工作期间内) return '工作时间内打卡异常';
         * 9
         *10          else if （工作 && 上或下班未打卡） return '上下班未打卡异常';
         *16       }
         *17  })
         *
         *
         */
        List<Object> empRegimeList = new ArrayList<>(10);// 1、获取公司全体人员排班列表

        List<Object> signInList = new ArrayList<>(10);// 2、打卡记录

        List<AcRegimeTimeVo> timeVos = new ArrayList<>();
        timeVos.add(new AcRegimeTimeVo());
        timeVos.add(null);
        timeVos.add(new AcRegimeTimeVo());
        timeVos.add(null);
        AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
        timeVo.setTimeFrom(LocalTime.now().minusHours(6));
        timeVo.setTimeTo(LocalTime.now().minusHours(5));
        timeVo.setExtendTimeFlag(false);
        timeVos.add(timeVo);
        timeVo = new AcRegimeTimeVo();
        timeVo.setTimeFrom(LocalTime.now().minusHours(4));
        timeVo.setTimeTo(LocalTime.now().minusHours(3));
        timeVo.setExtendTimeFlag(true);
        timeVos.add(timeVo);
        AcAttendanceRecordTemp a = new AcAttendanceRecordTemp();
        System.out.println(a);
        AcUtil.setTimeListToAttendanceAltTime(timeVos, a, false);
        System.out.println(a);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeFrom = LocalDateTime.parse("2020-10-22 08:15:00", df);
        LocalDateTime timeTo = LocalDateTime.parse("2020-10-22 12:00:00", df);
        LocalDateTime leaveFrom = LocalDateTime.parse("2020-10-22 00:00:00", df);
        LocalDateTime leaveTo = LocalDateTime.parse("2020-10-22 08:30:00", df);
        List<LocalDateTime> times = AcUtil.altTimeByLeave(timeFrom, timeTo, leaveFrom, leaveTo);
        for (LocalDateTime time : times) {
            System.out.println(time);
        }

        getIP();
    }

    public static void getIP() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("hostName:" + localhost.getHostName());
        System.out.println("hostAddress:" + localhost.getHostAddress());
    }


    @Test
    public void testCompanyAge1() {
        LocalDate a = LocalDate.now();
        System.out.println(a);
        System.out.println(a.getDayOfMonth());
        System.out.println(a.minusDays(a.getDayOfMonth() - 1));
    }


    @Test
    public void testCompanyAge12() throws Exception {
        FndUser user1 = new FndUser();
        user1.setId(1L);
        FndUser user2 = new FndUser();
        user2.setId(2L);
        System.out.println(user1);
        System.out.println(user2);
        user1.setId(user2.getId());
        user2.setId(3L);
        System.out.println(user1);
        System.out.println(user2);

        String originalSql = " ORDER BY AA AND ISNULL(aacr.req_code, '') = '' ORDER BY id ASC OFFSET 32000 ROWS";
        int lastIndexOf = originalSql.lastIndexOf("ORDER BY");
        String aa = originalSql.substring(0, lastIndexOf);
        String aa1 = originalSql.substring(lastIndexOf);
        System.out.println("****" + aa + "****");
        System.out.println("****" + aa1 + "****");

        String password = "123456";
        String encrypt = EncryptUtils.desEncrypt(password);
        String decrypt = EncryptUtils.desDecrypt(encrypt);
        System.out.println("password:" + password + "=======");
        System.out.println(" encrypt:" + encrypt + "=======");
        System.out.println(" decrypt:" + decrypt + "=======");


        encrypt = EncryptUtils.desEcbNoPaddingEncrypt(password);
        decrypt = EncryptUtils.desEcbNoPaddingDecrypt("9CBAF57E9D7C2619");
        System.out.println("password:" + password + "=======");
        System.out.println(" encrypt:" + encrypt + "=======");
        System.out.println(" decrypt:" + decrypt + "=======");

        String a = "123412351236";
        String b = "123";
        String c = a.startsWith(b) ? a.replaceFirst(b, "") : a;
        System.out.println(" a:" + a + "=======");
        System.out.println(" b:" + c + "=======");
        System.out.println(" b:" + c + "=======");
    }

    @Test
    public void aaa() throws Exception {

        HttpClient client = HttpClients.createDefault();

        HttpGet get = new HttpGet("http://172.18.1.131:8015/auth/verifyUser?password=9CBAF57E9D7C261&username=admin");
        HttpResponse response = client.execute(get);
        System.out.println(EntityUtils.toString(response.getEntity()));


//        HttpPost post = new HttpPost("http://172.18.1.131:8016/");
//        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//        parameters.add(new BasicNameValuePair("name", "丁丁"));
//        post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
//
//        HttpResponse response = client.execute(post);
//        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
