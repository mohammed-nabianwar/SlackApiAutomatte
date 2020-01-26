package com.SlackApiAutomate.util;

import org.testng.annotations.DataProvider;


public class DataProviderClass {

  @DataProvider
  public static Object[][] validNames() {
    return new Object[][] {
        new Object[] {"firsttest"},
        new Object[] {"first-test"},
        new Object[] {"first_test"},
        new Object[] {"-_first_test"},
        new Object[] {"1st-test"},
        new Object[] {"_1st-test-1st"}
    };
  }

  @DataProvider
  public static Object[][] invalidNames() {
    return new Object[][] {
        new Object[] {".,.,.,.,.,.,"},
        new Object[] {"@secondtest_"},
        new Object[] {"Second"},
        new Object[] {"second test"},
        new Object[] {"secondtest."}
    };
  }

  @DataProvider
  public static Object[][] renameChannels() {
    return new Object[][] {
        new Object[] {"firsttestrenamed"},
        new Object[] {"first-testrenamed"},
        new Object[] {"first_testrenamed"},
        new Object[] {"-_first_testrenamed"},
        new Object[] {"1st-testrenamed"},
        new Object[] {"_1st-test-1strenamed"}
    };
  }

}
