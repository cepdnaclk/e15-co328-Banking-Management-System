/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbSubModel;

/**
 *
 * @author RISITH-PC
 */
public class GraphModel {
    private String category;
    private Double number;

    public GraphModel() {
    }

    public GraphModel(String labels, Double numbers) {
        this.category = labels;
        this.number = numbers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    
}
