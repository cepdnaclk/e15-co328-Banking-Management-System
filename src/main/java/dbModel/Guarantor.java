
package dbModel;

public class Guarantor {
    private int guaId;
    private int takenId;
    private int memId;

    public Guarantor() {
    }

    public Guarantor(int guaId, int takenId, int memId) {
        this.guaId = guaId;
        this.takenId = takenId;
        this.memId = memId;
    }

    public int getGuaId() {
        return guaId;
    }

    public void setGuaId(int guaId) {
        this.guaId = guaId;
    }

    public int getTakenId() {
        return takenId;
    }

    public void setTakenId(int takenId) {
        this.takenId = takenId;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }
    
}
