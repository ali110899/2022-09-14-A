/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	
    	Album album = cmbA1.getValue();
    	
    	if(album.equals("")) {
    		txtResult.appendText("Selezionare un album\n");
    		return;
    	}
    	
    	Set<Album> connessa = model.getComponenteConnessa(album);
    	double somma=0.0;
    	for(Album a : connessa) {
    		somma=somma+a.getDurata();
    	}
    	
    	txtResult.appendText("Dimensione componente: "+connessa.size()+"\n");
    	txtResult.appendText("Durata totale: "+somma+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String durata = txtDurata.getText();
    	
    	if(durata.equals("")) {
    		txtResult.appendText("Valore 'd' obbligatorio\n");
    		return;
    	}
    	
    	Double duration=0.0;
    	try {
    		duration= Double.parseDouble(durata);
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire come valore 'd' un numero\n");
    	}
    	
    	model.creaGrafo(duration);
    	
    	List<Album> album = model.getAlbum();
    	cmbA1.getItems().clear();
    	cmbA1.getItems().addAll(album);
    	
    }

    @FXML
    void doEstraiSet(ActionEvent event) {

    	Album album = cmbA1.getValue();
    	
    	if(album.equals("")) {
    		txtResult.appendText("Selezionare un album\n");
    		return;
    	}
    	
    	String dTot = txtX.getText();
    	if(dTot.equals("")) {
    		txtResult.appendText("Specificare durata totale\n");
    		return;
    	}
    	
    	Double dTOT=0.0;
    	try {
    		dTOT= Double.parseDouble(dTot);
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un numero\n");
    	}
    	
    	Set<Album>  ottimi = model.ricercaMassimo(album, dTOT);
    	txtResult.appendText("Ottimi: "+ottimi+"\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
