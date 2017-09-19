package hello;

public class Roaster {

    private long id;
    public String getActivity1() {
		return activity1;
	}

	public void setActivity1(String activity1) {
		this.activity1 = activity1;
	}

	public String getActivity2() {
		return activity2;
	}

	public void setActivity2(String activity2) {
		this.activity2 = activity2;
	}

	private String activity1;
    private String activity2;
    private String content;
    private String date;

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
