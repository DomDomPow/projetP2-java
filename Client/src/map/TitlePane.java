package map;

import com.sun.xml.internal.ws.policy.PolicySubject;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Classe TitlePane héritant de StackPane.
 * Représente un titre affiché lors des différentes
 * étapes de la partie.
 */
public class TitlePane extends StackPane {

    /**
     * Constructeur du TitlePane.
     * Crée les différentes parties.
     * @param mainText : Texte principal.
     * @param secondaryText : Texte secondaire.
     */
    TitlePane(String mainText, String secondaryText){

        VBox vb = new VBox(8);

        // Les textes
        Text main = new Text(mainText);
        Text sec = new Text(secondaryText);

        main.setFont(Font.font("Open Sans Extrabold", FontWeight.NORMAL, 25));
        main.setFill(Color.WHITESMOKE);
        sec.setFont(Font.font("Open Sans", FontWeight.NORMAL, 15));
        sec.setFill(Color.WHITESMOKE);

        vb.setStyle("-fx-background-color: #000;");
        vb.setAlignment(Pos.CENTER);
        vb.setMinWidth(1072);
        vb.setMinHeight(800);

        vb.getChildren().addAll(main, sec);

        this.getChildren().add(vb);


    }
}
