package de.hadri.moduleapps;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author Hannes Drittler
 */
public class TestModuleManager {

    @Test
    public void testInterfaceDefault() {
        class MyTestModule implements Module {
        }
        MyTestModule testModule = new MyTestModule();
        ModuleProvider moduleProvider = new ModuleManager().initializeModules(Collections.singletonList(testModule));

        Assert.assertSame(testModule, moduleProvider.requestModule(MyTestModule.class, MyTestModule.class).get());
        Assert.assertSame(testModule, moduleProvider.requestModule(MyTestModule.class, Module.class).get());
    }

    @Test
    public void testRegisterFor() {
        class MyTestModule2 implements Module {
        }
        class MyTestModule implements Module {
            @Override
            public void registerNewModule(ModuleRegister register) {
                register.register(this, MyTestModule2.class);
            }
        }
        MyTestModule testModule = new MyTestModule();
        MyTestModule2 testModule2 = new MyTestModule2();
        ModuleProvider moduleProvider = new ModuleManager().initializeModules(Arrays.asList(testModule, testModule2));

        Assert.assertTrue(moduleProvider.requestModule(MyTestModule.class, MyTestModule2.class).isPresent());
        Assert.assertSame(testModule, moduleProvider.requestModule(MyTestModule.class, MyTestModule2.class).get());
        Assert.assertFalse(moduleProvider.requestModule(MyTestModule.class, MyTestModule.class).isPresent());
        Assert.assertFalse(moduleProvider.requestModule(MyTestModule.class, Module.class).isPresent());
    }

    @Test
    public void testSolveDependency() {
        final boolean[] isSolved = {false};
        class MyTestModule2 implements Module {
        }
        MyTestModule2 testModule2 = new MyTestModule2();
        class MyTestModule implements Module {
            @Override
            public void registerNewModule(ModuleRegister register) {
                register.register(this, MyTestModule.class);
            }
            @Override
            public void solveDependencies(ModuleProvider provider) {
                Assert.assertSame(this, provider.requestModule(MyTestModule.class, MyTestModule.class).get());
                Assert.assertSame(testModule2, provider.requestModule(MyTestModule2.class, MyTestModule.class).get());
                isSolved[0] = true;
            }
        }
        MyTestModule testModule = new MyTestModule();
        ModuleProvider moduleProvider = new ModuleManager().initializeModules(Arrays.asList(testModule, testModule2));

        Assert.assertTrue(isSolved[0]);
        Assert.assertSame(testModule2, moduleProvider.requestModule(MyTestModule2.class, MyTestModule.class).get());
        Assert.assertSame(testModule, moduleProvider.requestModule(MyTestModule.class, MyTestModule.class).get());
    }

    @Test
    public void testSolveDependencyWithConfig() throws Exception {
        TestModule1 testModule1 = new TestModule1();
        TestModule2 testModule2 = new TestModule2();
        ModuleProvider moduleProvider = new ModuleManager().initializeModules(Arrays.asList(testModule1, testModule2));

        Assert.assertTrue(testModule1.solved);
        Assert.assertTrue(testModule2.solved);
        Assert.assertSame(TestModule2.TEST_ID, moduleProvider.requestModule(TestModule2.class, TestModule.class).get().itentify());
        Assert.assertSame(TestModule1.TEST_ID, moduleProvider.requestModule(TestModule1.class, TestModule.class).get().itentify());
        Assert.assertTrue(List.of(TestModule1.TEST_ID, TestModule2.TEST_ID)
                .contains(moduleProvider.requestModule(TestModule.class, TestModule.class).get().itentify()));

        String key = TestModule1.class.getName() + "/" + TestModule.class.getName();
        Assert.assertSame(TestModule1.TEST_ID, moduleProvider.requestModuleByConfig(TestModule.class, TestModule1.class,
                getProperties(Map.of(key, TestModule1.class.getName()))).get().itentify());
        Assert.assertSame(TestModule2.TEST_ID, moduleProvider.requestModuleByConfig(TestModule.class, TestModule1.class,
                getProperties(Map.of(key, TestModule2.class.getName()))).get().itentify());
    }

    private Properties getProperties(Map<String, String> config) throws IOException {
        Properties properties = new Properties();
        StringBuilder sb = new StringBuilder();
        config.forEach((k, v) -> sb.append(k).append(" = ").append(v).append('\n'));
        properties.load(new StringReader(sb.toString()));
        return properties;
    }

    static abstract class TestModule implements Module {
        abstract String itentify();
    }

    static class TestModule1 extends TestModule implements Module {
        public static final String TEST_ID = "test1";
        public boolean solved = false;
        @Override
        public void solveDependencies(ModuleProvider provider) {
            Assert.assertSame(TEST_ID, provider.requestModule(TestModule1.class, TestModule1.class).get().itentify());
            solved = true;
        }
        @Override
        public String itentify() {
            return TEST_ID;
        }
    }

    static class TestModule2 extends TestModule {
        public static final String TEST_ID = "test2";
        public boolean solved = false;
        @Override
        public void solveDependencies(ModuleProvider provider) {
            Assert.assertSame(TestModule1.TEST_ID, provider.requestModule(TestModule1.class, TestModule2.class).get().itentify());
            Assert.assertSame(TestModule2.TEST_ID, provider.requestModule(TestModule2.class, TestModule2.class).get().itentify());
            solved = true;
        }
        @Override
        public String itentify() {
            return TEST_ID;
        }
    }

}
