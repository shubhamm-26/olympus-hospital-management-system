package hospital.Lab;

public class LabTests {
    private long test_id;
    private String test_name;

    public LabTests(long test_id, String test_name) {
        this.test_id = test_id;
        this.test_name = test_name;
    }

    public long getTest_id() {
        return test_id;
    }

    public void setTest_id(long test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }
}
