/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.core.profile;

import com.google.common.base.Joiner;
import org.apache.skywalking.oap.server.core.ThreadStackAnalyzer;
import org.junit.Test;
import org.mockito.internal.util.io.IOUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author MrPro
 */
public class ThreadAnalyzerTestCase {

    @Test
    public void testAnalyze() {

        // deserialize snapshot list
        InputStream expectedInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("thread-snapshot.yml");
        System.out.println("start reading file");
        long startTime = System.currentTimeMillis();
        final ProfileTaskSegmentSnapshotDataHolder dataHolder = new Yaml().loadAs(expectedInputStream, ProfileTaskSegmentSnapshotDataHolder.class);
        System.out.print("reading file finish:");
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        System.out.println("starting transform");
        List<ProfileTaskSegmentSnapshotRecord> records = dataHolder.transform();
        System.out.print("transform finished: ");
        System.out.println(System.currentTimeMillis() - startTime);

        // analyze records
        startTime = System.currentTimeMillis();
        ProfileAnalyze analyze = new ThreadStackAnalyzer().analyze(records);
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(1);
    }


    public static void methodA() throws InterruptedException {
//        TimeUnit.MILLISECONDS.sleep(20);
//        for (int i = 0; i < 3; i++) {
            methodB(0);
//        }
//        TimeUnit.MILLISECONDS.sleep(50);
    }

    public static void methodB(int count) throws InterruptedException {
        if (count > 2) {
            return;
        }
//        TimeUnit.MILLISECONDS.sleep(50);
//        for (int i = 0; i < 2; i++) {
//            methodC();
//        }
        TimeUnit.MILLISECONDS.sleep(30);
//        methodC();

        methodB(count + 1);
    }

    public static void methodC() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(30);
    }

    private static final byte[] bytes = "ABCDEFGHJKMNPRSTUVWXYZabcdefghjkmnprstuvwxyz".getBytes();

    private static String random(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(bytes[new Random().nextInt(bytes.length)]);
        }
        return sb.toString();
    }

    public static void method771167811997() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        TimeUnit.MINUTES.sleep(4);
    }

    public static void method8397698370() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method771167811997();
    }

    public static void method10711810474110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8397698370();
    }

    public static void method12187106117106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10711810474110();
    }

    public static void method8889119121114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12187106117106();
    }

    public static void method727711711784() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8889119121114();
    }

    public static void method72787411677() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method727711711784();
    }

    public static void method861021067497() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72787411677();
    }

    public static void method651121177599() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method861021067497();
    }

    public static void method8511783101114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method651121177599();
    }

    public static void method1001016784104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8511783101114();
    }

    public static void method10999886689() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1001016784104();
    }

    public static void method72116706798() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10999886689();
    }

    public static void method8610768100104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72116706798();
    }

    public static void method10412090106106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8610768100104();
    }

    public static void method11572109114115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10412090106106();
    }

    public static void method1181121198366() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11572109114115();
    }

    public static void method1201027574112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1181121198366();
    }

    public static void method7478906588() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1201027574112();
    }

    public static void method82991098572() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7478906588();
    }

    public static void method121101121122121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82991098572();
    }

    public static void method69901067597() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method121101121122121();
    }

    public static void method68100104118106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method69901067597();
    }

    public static void method801208311770() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68100104118106();
    }

    public static void method106897011086() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method801208311770();
    }

    public static void method103109998299() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method106897011086();
    }

    public static void method681127410072() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103109998299();
    }

    public static void method981181018275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method681127410072();
    }

    public static void method867210211488() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method981181018275();
    }

    public static void method821208810968() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method867210211488();
    }

    public static void method831038865121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821208810968();
    }

    public static void method9777986877() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method831038865121();
    }

    public static void method66851108099() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9777986877();
    }

    public static void method8812010269112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method66851108099();
    }

    public static void method121112778880() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8812010269112();
    }

    public static void method1156610910183() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method121112778880();
    }

    public static void method7268115112118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1156610910183();
    }

    public static void method119827174101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7268115112118();
    }

    public static void method9910967119101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method119827174101();
    }

    public static void method10272656967() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9910967119101();
    }

    public static void method711161189974() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10272656967();
    }

    public static void method821001167299() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method711161189974();
    }

    public static void method102115688272() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821001167299();
    }

    public static void method114721178767() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102115688272();
    }

    public static void method847011211482() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114721178767();
    }

    public static void method7284746565() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method847011211482();
    }

    public static void method10910010110666() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7284746565();
    }

    public static void method1006610697102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10910010110666();
    }

    public static void method78721178387() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1006610697102();
    }

    public static void method7210310665101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method78721178387();
    }

    public static void method8798987065() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7210310665101();
    }

    public static void method116889885103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8798987065();
    }

    public static void method1048478106106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method116889885103();
    }

    public static void method1221036586103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1048478106106();
    }

    public static void method1148310390116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1221036586103();
    }

    public static void method74717111670() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1148310390116();
    }

    public static void method12289827289() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method74717111670();
    }

    public static void method708512198110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12289827289();
    }

    public static void method8510165122101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method708512198110();
    }

    public static void method120677510475() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8510165122101();
    }

    public static void method114821068097() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method120677510475();
    }

    public static void method708966116110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114821068097();
    }

    public static void method1211187570116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method708966116110();
    }

    public static void method8869887186() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1211187570116();
    }

    public static void method10767118114118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8869887186();
    }

    public static void method837766118114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10767118114118();
    }

    public static void method691217766120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method837766118114();
    }

    public static void method7171879980() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method691217766120();
    }

    public static void method701149011072() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7171879980();
    }

    public static void method11668101104101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method701149011072();
    }

    public static void method1101191096586() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11668101104101();
    }

    public static void method1156911710369() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1101191096586();
    }

    public static void method10169120102118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1156911710369();
    }

    public static void method68102907899() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10169120102118();
    }

    public static void method120786670116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68102907899();
    }

    public static void method976810910469() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method120786670116();
    }

    public static void method10711674115120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method976810910469();
    }

    public static void method667812280117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10711674115120();
    }

    public static void method686710398106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method667812280117();
    }

    public static void method74728911269() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method686710398106();
    }

    public static void method101115697784() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method74728911269();
    }

    public static void method691066886119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method101115697784();
    }

    public static void method1069711911972() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method691066886119();
    }

    public static void method1201101078486() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1069711911972();
    }

    public static void method69107758797() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1201101078486();
    }

    public static void method6867709868() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method69107758797();
    }

    public static void method120748611766() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6867709868();
    }

    public static void method971127210983() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method120748611766();
    }

    public static void method7810410075110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method971127210983();
    }

    public static void method112101806685() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7810410075110();
    }

    public static void method11510610612278() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method112101806685();
    }

    public static void method1201018872103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11510610612278();
    }

    public static void method1071021196680() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1201018872103();
    }

    public static void method821076670122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1071021196680();
    }

    public static void method71708090102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821076670122();
    }

    public static void method86701168698() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method71708090102();
    }

    public static void method122706974116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method86701168698();
    }

    public static void method891001096570() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method122706974116();
    }

    public static void method69809010470() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method891001096570();
    }

    public static void method807512012289() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method69809010470();
    }

    public static void method107114759983() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method807512012289();
    }

    public static void method11571707167() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method107114759983();
    }

    public static void method1191151039988() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11571707167();
    }

    public static void method1077412288114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1191151039988();
    }

    public static void method83779011987() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1077412288114();
    }

    public static void method89717165119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method83779011987();
    }

    public static void method8680807780() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89717165119();
    }

    public static void method8966886780() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8680807780();
    }

    public static void method877271102106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8966886780();
    }

    public static void method1149910011975() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method877271102106();
    }

    public static void method86838911070() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1149910011975();
    }

    public static void method7282759082() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method86838911070();
    }

    public static void method847411811568() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7282759082();
    }

    public static void method8265907897() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method847411811568();
    }

    public static void method82878011774() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8265907897();
    }

    public static void method907210480117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82878011774();
    }

    public static void method789712065115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method907210480117();
    }

    public static void method1221078511870() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method789712065115();
    }

    public static void method9911710282122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1221078511870();
    }

    public static void method98826987122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9911710282122();
    }

    public static void method678512010071() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method98826987122();
    }

    public static void method11710411010985() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method678512010071();
    }

    public static void method688212110183() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11710411010985();
    }

    public static void method1178310710677() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method688212110183();
    }

    public static void method658511267114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1178310710677();
    }

    public static void method7289115102101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method658511267114();
    }

    public static void method686810112066() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7289115102101();
    }

    public static void method10790120109110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method686810112066();
    }

    public static void method88687272109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10790120109110();
    }

    public static void method977410175120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method88687272109();
    }

    public static void method821098812187() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method977410175120();
    }

    public static void method77668068114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821098812187();
    }

    public static void method10011786109116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method77668068114();
    }

    public static void method115102999865() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10011786109116();
    }

    public static void method7870906570() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method115102999865();
    }

    public static void method7580728786() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7870906570();
    }

    public static void method11066999966() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7580728786();
    }

    public static void method9883869789() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11066999966();
    }

    public static void method688683115114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9883869789();
    }

    public static void method107120897787() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method688683115114();
    }

    public static void method1021211029978() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method107120897787();
    }

    public static void method1221008869116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1021211029978();
    }

    public static void method10669888472() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1221008869116();
    }

    public static void method697010988117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10669888472();
    }

    public static void method72107909077() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method697010988117();
    }

    public static void method89878311768() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72107909077();
    }

    public static void method82118667797() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89878311768();
    }

    public static void method8911470104109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82118667797();
    }

    public static void method6711411568115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8911470104109();
    }

    public static void method1021147411066() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6711411568115();
    }

    public static void method102100906668() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1021147411066();
    }

    public static void method1068910278122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102100906668();
    }

    public static void method80122908378() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1068910278122();
    }

    public static void method7887978083() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method80122908378();
    }

    public static void method1041041197178() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7887978083();
    }

    public static void method7874827872() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1041041197178();
    }

    public static void method11077808598() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7874827872();
    }

    public static void method116781209997() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11077808598();
    }

    public static void method122771048590() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method116781209997();
    }

    public static void method821018911487() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method122771048590();
    }

    public static void method801037083103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821018911487();
    }

    public static void method84701018768() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method801037083103();
    }

    public static void method12265117118117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method84701018768();
    }

    public static void method82102707478() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12265117118117();
    }

    public static void method981007566122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82102707478();
    }

    public static void method876611688106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method981007566122();
    }

    public static void method1159011480107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method876611688106();
    }

    public static void method871181068275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1159011480107();
    }

    public static void method727211898118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method871181068275();
    }

    public static void method11210412086119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method727211898118();
    }

    public static void method831108384122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11210412086119();
    }

    public static void method103891167465() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method831108384122();
    }

    public static void method821229969106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103891167465();
    }

    public static void method11684838682() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821229969106();
    }

    public static void method841008090106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11684838682();
    }

    public static void method6811683116104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method841008090106();
    }

    public static void method88901007882() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6811683116104();
    }

    public static void method90721167072() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method88901007882();
    }

    public static void method89807511069() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method90721167072();
    }

    public static void method901176912197() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89807511069();
    }

    public static void method8582103122106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method901176912197();
    }

    public static void method106109698488() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8582103122106();
    }

    public static void method101678871122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method106109698488();
    }

    public static void method82103686598() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method101678871122();
    }

    public static void method104851106688() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82103686598();
    }

    public static void method11282115107102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method104851106688();
    }

    public static void method84878011780() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11282115107102();
    }

    public static void method103881097574() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method84878011780();
    }

    public static void method121729711597() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103881097574();
    }

    public static void method6574867175() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method121729711597();
    }

    public static void method100781067465() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6574867175();
    }

    public static void method8470678765() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method100781067465();
    }

    public static void method109118120100107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8470678765();
    }

    public static void method1001018410666() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method109118120100107();
    }

    public static void method68122668572() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1001018410666();
    }

    public static void method1121099911883() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68122668572();
    }

    public static void method8310488116109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1121099911883();
    }

    public static void method1079897104107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8310488116109();
    }

    public static void method102828288114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1079897104107();
    }

    public static void method8290866788() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102828288114();
    }

    public static void method11665978582() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8290866788();
    }

    public static void method10611211811984() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11665978582();
    }

    public static void method8698109121100() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10611211811984();
    }

    public static void method1101077785120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8698109121100();
    }

    public static void method1151091148287() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1101077785120();
    }

    public static void method10671678667() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1151091148287();
    }

    public static void method11265867278() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10671678667();
    }

    public static void method1209810410197() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11265867278();
    }

    public static void method97711207785() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1209810410197();
    }

    public static void method121116826682() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method97711207785();
    }

    public static void method1098612275114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method121116826682();
    }

    public static void method72117788984() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1098612275114();
    }

    public static void method117122727574() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72117788984();
    }

    public static void method1159710311083() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method117122727574();
    }

    public static void method115102116106104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1159710311083();
    }

    public static void method114998910790() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method115102116106104();
    }

    public static void method90866875119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114998910790();
    }

    public static void method112828911666() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method90866875119();
    }

    public static void method120899798102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method112828911666();
    }

    public static void method721036787112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method120899798102();
    }

    public static void method846611267100() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method721036787112();
    }

    public static void method9811968112121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method846611267100();
    }

    public static void method11011811210980() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9811968112121();
    }

    public static void method8299996870() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11011811210980();
    }

    public static void method114691068285() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8299996870();
    }

    public static void method117121997769() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114691068285();
    }

    public static void method701188011574() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method117121997769();
    }

    public static void method1108966103100() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method701188011574();
    }

    public static void method10699806688() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1108966103100();
    }

    public static void method6911790116118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10699806688();
    }

    public static void method8368678468() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6911790116118();
    }

    public static void method901206510065() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8368678468();
    }

    public static void method68889011084() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method901206510065();
    }

    public static void method721226810769() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68889011084();
    }

    public static void method707872107102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method721226810769();
    }

    public static void method861047010277() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method707872107102();
    }

    public static void method75868312275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method861047010277();
    }

    public static void method85109687585() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method75868312275();
    }

    public static void method838611510787() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method85109687585();
    }

    public static void method83118112102104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method838611510787();
    }

    public static void method728684114117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method83118112102104();
    }

    public static void method1171066574112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method728684114117();
    }

    public static void method801078977104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1171066574112();
    }

    public static void method69701158389() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method801078977104();
    }

    public static void method1071017510489() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method69701158389();
    }

    public static void method11899100119115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1071017510489();
    }

    public static void method1171141178298() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11899100119115();
    }

    public static void method86115114109117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1171141178298();
    }

    public static void method101107119119120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method86115114109117();
    }

    public static void method10011610067119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method101107119119120();
    }

    public static void method1038910012174() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10011610067119();
    }

    public static void method10610782112109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1038910012174();
    }

    public static void method78103988767() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10610782112109();
    }

    public static void method12199857471() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method78103988767();
    }

    public static void method10988807184() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12199857471();
    }

    public static void method707767107104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10988807184();
    }

    public static void method88109707475() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method707767107104();
    }

    public static void method846710169104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method88109707475();
    }

    public static void method10210911297121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method846710169104();
    }

    public static void method651067411098() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10210911297121();
    }

    public static void method831037012289() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method651067411098();
    }

    public static void method9811411970120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method831037012289();
    }

    public static void method718911211084() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9811411970120();
    }

    public static void method1011218871122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method718911211084();
    }

    public static void method12274120117120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1011218871122();
    }

    public static void method7299806986() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12274120117120();
    }

    public static void method1098611612086() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7299806986();
    }

    public static void method72107677169() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1098611612086();
    }

    public static void method106907410797() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72107677169();
    }

    public static void method11467906684() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method106907410797();
    }

    public static void method9077886585() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11467906684();
    }

    public static void method74821009999() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9077886585();
    }

    public static void method651208411267() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method74821009999();
    }

    public static void method687410212167() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method651208411267();
    }

    public static void method1186610497102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method687410212167();
    }

    public static void method74836782121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1186610497102();
    }

    public static void method97688711577() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method74836782121();
    }

    public static void method83857782122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method97688711577();
    }

    public static void method12077697067() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method83857782122();
    }

    public static void method103776971102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12077697067();
    }

    public static void method6710489120109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103776971102();
    }

    public static void method11978907787() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6710489120109();
    }

    public static void method1187510911698() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11978907787();
    }

    public static void method8774107106100() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1187510911698();
    }

    public static void method80826511082() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8774107106100();
    }

    public static void method12010211065118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method80826511082();
    }

    public static void method98114747885() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12010211065118();
    }

    public static void method97121868788() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method98114747885();
    }

    public static void method861099911899() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method97121868788();
    }

    public static void method67871226785() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method861099911899();
    }

    public static void method9810088112103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method67871226785();
    }

    public static void method7410610910286() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9810088112103();
    }

    public static void method107657711988() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7410610910286();
    }

    public static void method102681028870() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method107657711988();
    }

    public static void method11784688567() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102681028870();
    }

    public static void method808882119109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11784688567();
    }

    public static void method10788909082() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method808882119109();
    }

    public static void method741186574117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10788909082();
    }

    public static void method1201067790121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method741186574117();
    }

    public static void method89826611965() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1201067790121();
    }

    public static void method1218512072121() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89826611965();
    }

    public static void method881047510469() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1218512072121();
    }

    public static void method68698211274() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method881047510469();
    }

    public static void method7286101117103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68698211274();
    }

    public static void method119979911083() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7286101117103();
    }

    public static void method997711669107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method119979911083();
    }

    public static void method1207184116104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method997711669107();
    }

    public static void method97998811778() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1207184116104();
    }

    public static void method99122778682() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method97998811778();
    }

    public static void method878511869107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method99122778682();
    }

    public static void method7871668367() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method878511869107();
    }

    public static void method6710611668117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7871668367();
    }

    public static void method10210077101102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6710611668117();
    }

    public static void method75718610772() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10210077101102();
    }

    public static void method1101039888102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method75718610772();
    }

    public static void method666812167106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1101039888102();
    }

    public static void method1008911077103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method666812167106();
    }

    public static void method901068868122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1008911077103();
    }

    public static void method8011412011980() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method901068868122();
    }

    public static void method881091208977() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8011412011980();
    }

    public static void method89678889114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method881091208977();
    }

    public static void method8578829998() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89678889114();
    }

    public static void method10675106118109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8578829998();
    }

    public static void method82828077106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10675106118109();
    }

    public static void method72676811088() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82828077106();
    }

    public static void method88986810185() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72676811088();
    }

    public static void method1151188797101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method88986810185();
    }

    public static void method115977577119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1151188797101();
    }

    public static void method878710767106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method115977577119();
    }

    public static void method716611599102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method878710767106();
    }

    public static void method981076910790() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method716611599102();
    }

    public static void method679910278112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method981076910790();
    }

    public static void method757474110122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method679910278112();
    }

    public static void method104891207074() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method757474110122();
    }

    public static void method1011068480114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method104891207074();
    }

    public static void method99837777117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1011068480114();
    }

    public static void method80112728367() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method99837777117();
    }

    public static void method112871168269() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method80112728367();
    }

    public static void method11489728475() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method112871168269();
    }

    public static void method696611911265() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11489728475();
    }

    public static void method728070109107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method696611911265();
    }

    public static void method651217011099() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method728070109107();
    }

    public static void method116717810465() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method651217011099();
    }

    public static void method887511711583() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method116717810465();
    }

    public static void method8690859998() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method887511711583();
    }

    public static void method1077411611698() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8690859998();
    }

    public static void method9898827270() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1077411611698();
    }

    public static void method84117677770() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9898827270();
    }

    public static void method677187117104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method84117677770();
    }

    public static void method751159989110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method677187117104();
    }

    public static void method8098858677() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method751159989110();
    }

    public static void method746684121122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8098858677();
    }

    public static void method898810066110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method746684121122();
    }

    public static void method10182658697() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method898810066110();
    }

    public static void method10711874102120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10182658697();
    }

    public static void method66831188471() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10711874102120();
    }

    public static void method901158010774() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method66831188471();
    }

    public static void method90681096865() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method901158010774();
    }

    public static void method1141191176668() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method90681096865();
    }

    public static void method80868310675() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1141191176668();
    }

    public static void method987112212275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method80868310675();
    }

    public static void method100829966117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method987112212275();
    }

    public static void method8910399101110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method100829966117();
    }

    public static void method83721149777() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8910399101110();
    }

    public static void method6910011210483() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method83721149777();
    }

    public static void method858310711799() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6910011210483();
    }

    public static void method718389101109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method858310711799();
    }

    public static void method701149997118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method718389101109();
    }

    public static void method7010212286106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method701149997118();
    }

    public static void method1071151208569() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7010212286106();
    }

    public static void method1158612270112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1071151208569();
    }

    public static void method11710610610968() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1158612270112();
    }

    public static void method65869912097() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11710610610968();
    }

    public static void method1109710411886() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method65869912097();
    }

    public static void method97676910487() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1109710411886();
    }

    public static void method90115998772() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method97676910487();
    }

    public static void method877111568110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method90115998772();
    }

    public static void method9878121116122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method877111568110();
    }

    public static void method119751157267() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9878121116122();
    }

    public static void method677510411866() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method119751157267();
    }

    public static void method6583976880() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method677510411866();
    }

    public static void method1048912272104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6583976880();
    }

    public static void method898712111588() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1048912272104();
    }

    public static void method8687747788() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method898712111588();
    }

    public static void method1048611888122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8687747788();
    }

    public static void method11710710088104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1048611888122();
    }

    public static void method1221078066112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11710710088104();
    }

    public static void method85866911997() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1221078066112();
    }

    public static void method1091188010183() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method85866911997();
    }

    public static void method8389786869() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1091188010183();
    }

    public static void method122886911575() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8389786869();
    }

    public static void method114651036868() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method122886911575();
    }

    public static void method1208982100102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114651036868();
    }

    public static void method7578100118114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1208982100102();
    }

    public static void method881007410766() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7578100118114();
    }

    public static void method107878710770() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method881007410766();
    }

    public static void method1061071209999() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method107878710770();
    }

    public static void method677712210274() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1061071209999();
    }

    public static void method11282110106109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method677712210274();
    }

    public static void method1026772107114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11282110106109();
    }

    public static void method82898911275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1026772107114();
    }

    public static void method801179711589() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82898911275();
    }

    public static void method7480122118116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method801179711589();
    }

    public static void method101886911786() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7480122118116();
    }

    public static void method1228410683101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method101886911786();
    }

    public static void method65101898897() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1228410683101();
    }

    public static void method103657189104() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method65101898897();
    }

    public static void method1177811810378() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103657189104();
    }

    public static void method1126810388114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1177811810378();
    }

    public static void method831091108697() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1126810388114();
    }

    public static void method7775121100119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method831091108697();
    }

    public static void method868867115114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7775121100119();
    }

    public static void method86841187170() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method868867115114();
    }

    public static void method68727510966() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method86841187170();
    }

    public static void method1181071126868() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68727510966();
    }

    public static void method99878467109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1181071126868();
    }

    public static void method6610410175101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method99878467109();
    }

    public static void method12174828677() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6610410175101();
    }

    public static void method1021091077882() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12174828677();
    }

    public static void method82998974102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1021091077882();
    }

    public static void method85102106115118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method82998974102();
    }

    public static void method6710198121117() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method85102106115118();
    }

    public static void method1151208711275() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6710198121117();
    }

    public static void method651091036986() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1151208711275();
    }

    public static void method10210211497106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method651091036986();
    }

    public static void method70659711071() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10210211497106();
    }

    public static void method8211511872110() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method70659711071();
    }

    public static void method691061179887() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method8211511872110();
    }

    public static void method9011011010683() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method691061179887();
    }

    public static void method66121119114102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9011011010683();
    }

    public static void method70859710972() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method66121119114102();
    }

    public static void method1067811411080() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method70859710972();
    }

    public static void method87106118119122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1067811411080();
    }

    public static void method89897186115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method87106118119122();
    }

    public static void method977582119118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89897186115();
    }

    public static void method1107511877106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method977582119118();
    }

    public static void method1011038711084() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1107511877106();
    }

    public static void method86698812180() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1011038711084();
    }

    public static void method691128310088() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method86698812180();
    }

    public static void method1226710371118() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method691128310088();
    }

    public static void method115871157097() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1226710371118();
    }

    public static void method1091179011065() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method115871157097();
    }

    public static void method120891019984() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1091179011065();
    }

    public static void method1181078371103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method120891019984();
    }

    public static void method11411612189106() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1181078371103();
    }

    public static void method119109708787() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11411612189106();
    }

    public static void method668011610471() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method119109708787();
    }

    public static void method12167697074() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method668011610471();
    }

    public static void method6667679083() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12167697074();
    }

    public static void method838510970115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6667679083();
    }

    public static void method1157811910274() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method838510970115();
    }

    public static void method1071141008686() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1157811910274();
    }

    public static void method9911287109103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1071141008686();
    }

    public static void method7870112119119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9911287109103();
    }

    public static void method89104906987() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7870112119119();
    }

    public static void method1001097211568() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method89104906987();
    }

    public static void method1096511411483() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1001097211568();
    }

    public static void method9710111911569() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1096511411483();
    }

    public static void method10910410965116() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9710111911569();
    }

    public static void method80778310169() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method10910410965116();
    }

    public static void method821031099878() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method80778310169();
    }

    public static void method751228989103() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method821031099878();
    }

    public static void method103878680122() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method751228989103();
    }

    public static void method1171041017198() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method103878680122();
    }

    public static void method991127766101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1171041017198();
    }

    public static void method1007810310971() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method991127766101();
    }

    public static void method11899109107112() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1007810310971();
    }

    public static void method806710611465() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method11899109107112();
    }

    public static void method1166711978114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method806710611465();
    }

    public static void method68118107116109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1166711978114();
    }

    public static void method12287888677() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method68118107116109();
    }

    public static void method1028411485100() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12287888677();
    }

    public static void method881171028777() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1028411485100();
    }

    public static void method67117119107114() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method881171028777();
    }

    public static void method75114858499() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method67117119107114();
    }

    public static void method6585889888() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method75114858499();
    }

    public static void method9787778865() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6585889888();
    }

    public static void method72706980119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9787778865();
    }

    public static void method671107410365() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method72706980119();
    }

    public static void method102988011077() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method671107410365();
    }

    public static void method6786728089() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102988011077();
    }

    public static void method679710266119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6786728089();
    }

    public static void method1221078810271() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method679710266119();
    }

    public static void method7410312110484() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1221078810271();
    }

    public static void method114771026570() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method7410312110484();
    }

    public static void method77727511474() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method114771026570();
    }

    public static void method104698068120() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method77727511474();
    }

    public static void method90121856774() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method104698068120();
    }

    public static void method12068109112115() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method90121856774();
    }

    public static void method6872101119119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method12068109112115();
    }

    public static void method84886687107() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method6872101119119();
    }

    public static void method66771189983() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method84886687107();
    }

    public static void method1048311969119() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method66771189983();
    }

    public static void method88109121102101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method1048311969119();
    }

    public static void method102899811872() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method88109121102101();
    }

    public static void method77115122103101() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method102899811872();
    }

    public static void method9766717198() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method77115122103101();
    }

    public static void method83866870109() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method9766717198();
    }

    public static void method117110836697() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method83866870109();
    }

    public static void method122698689102() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        method117110836697();
    }


    public static void main(String[] args) {

//        String beforeMethod = null;
//        for (int i = 0; i < 500; i++) {
//            String methodName = "method" + random(5);
//            System.out.println("    public static void " + methodName +"() throws InterruptedException {\n" +
//                    "        TimeUnit.MILLISECONDS.sleep(100);\n" +
//                            (beforeMethod != null ? beforeMethod + "();\n" : "") +
//                            (i == 0 ? "TimeUnit.MILLISECONDS.sleep(60000);\n" : "") +
//                    "    }\n");
//            beforeMethod = methodName;
//        }

        // deserialize snapshot list
        InputStream expectedInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("thread-snapshot.yml");
        System.out.println("start reading file");
        long startTime = System.currentTimeMillis();
        final ProfileTaskSegmentSnapshotDataHolder dataHolder = new Yaml().loadAs(expectedInputStream, ProfileTaskSegmentSnapshotDataHolder.class);
        System.out.print("reading file finish:");
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        System.out.println("starting transform");
        List<ProfileTaskSegmentSnapshotRecord> records = dataHolder.transform();
        System.out.print("transform finished: ");
        System.out.println(System.currentTimeMillis() - startTime);

        // analyze records
        startTime = System.currentTimeMillis();
        ProfileAnalyze analyze = new ThreadStackAnalyzer().analyze(records);
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(1);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    method122698689102();
//                } catch (InterruptedException e) {
//                }
//            }
//        });
//
//        thread.start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try (
//                    FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/liuhan/Documents/idea_workspace/lagou_new_workspace/skywalking-liuhan/skywalking/oap-server/server-core/src/test/resources/thread-snapshot.yml"));
//                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//                ) {
//                    bufferedOutputStream.write("list:\n".getBytes());
//                    int seq = 0;
//                    long currentLoopStartTime = -1;
//                    while (thread.isAlive()) {
//                        currentLoopStartTime = System.currentTimeMillis();
//                        StackTraceElement[] stack = thread.getStackTrace();
//
//                        StringBuilder stringBuilder = new StringBuilder();
//                        stringBuilder.append("    - dumpTime: ").append(System.currentTimeMillis()).append("\n");
//                        stringBuilder.append("      sequence: ").append(seq++).append("\n");
//                        stringBuilder.append("      stackList:").append("\n");
//                        for (int i = stack.length - 1; i >= 0; i--) {
//                            stringBuilder.append("        - ").append(stack[i].getClassName()).append(".").append(stack[i].getMethodName()).append(":").append(stack[i].getLineNumber()).append("\n");
//                        }
//
//                        long needToSleep = (currentLoopStartTime + 10) - System.currentTimeMillis();
//                        needToSleep = needToSleep > 0 ? needToSleep : 10;
//                        try {
//                            Thread.sleep(needToSleep);
//                        } catch (InterruptedException e) {
//                        }
//                        bufferedOutputStream.write(stringBuilder.toString().getBytes());
//                        System.out.println(seq);
//                    }
//
//                    fileOutputStream.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
    }
}
