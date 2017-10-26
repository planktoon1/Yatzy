package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.YatzyDice;

public class MainApp extends Application {
	private final Controller controller = new Controller();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Yatzy");
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	// -------------------------------------------------------------------------

	// Shows the face values of the 5 dice.
	private final TextField[] txfValues = new TextField[5];
	// Shows the hold status of the 5 dice.
	private final CheckBox[] cbxHolds = new CheckBox[5];
	// Shows the obtained results.
	// For results not set yet, the possible result of
	// the actual face values of the 5 dice are shown.
	private final TextField[] txfResults = new TextField[18];

	private final Label[] lblResults = new Label[18];
	// Shows points in sums, bonus and total.
	private final TextField txfSumSame = new TextField(), txfBonus = new TextField(), txfSumOther = new TextField(),
			txfTotal = new TextField();

	// private final Label lblRolled = new Label();
	// private final Button btnRoll = new Button(" Roll ");

	private void initContent(GridPane pane) {
		// pane.setGridLinesVisible(true);
		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);

		// ---------------------------------------------------------------------

		GridPane dicePane = new GridPane();
		pane.add(dicePane, 0, 0);
		// dicePane.setGridLinesVisible(true);
		dicePane.setPadding(new Insets(10));
		dicePane.setHgap(10);
		dicePane.setVgap(10);
		dicePane.setStyle("-fx-border-color: black");

		for (int i = 0; i < 5; i++) {
			txfValues[i] = new TextField();
			dicePane.add(txfValues[i], i, 0);
			txfValues[i].setEditable(false);
			txfValues[i].setMinSize(50, 50);
			txfValues[i].setMaxSize(50, 50);

			cbxHolds[i] = new CheckBox();
			dicePane.add(cbxHolds[i], i, 1);
			cbxHolds[i].setText("Hold");
		}

		Label lblRolled = new Label("Rolled:");
		dicePane.add(lblRolled, 5, 1);

		Button btnRoll = new Button("Roll");
		dicePane.add(btnRoll, 5, 0);

		// ---------------------------------------------------------------------

		GridPane scorePane = new GridPane();
		pane.add(scorePane, 0, 1);
		// scorePane.setGridLinesVisible(true);
		scorePane.setPadding(new Insets(10));
		scorePane.setVgap(5);
		scorePane.setHgap(10);
		scorePane.setStyle("-fx-border-color: black");
		int w = 50; // width of the text fields

		String[] names = { "1'ere", "2'ere", "3'ere", "4'ere", "5'ere", "6'ere", "Sum", "Bonus", "1 par", "2 par",
				"3 ens", "4 ens", "Fuldt hus", "Small straight", "Large straight", "Chance", "Yatzy", "Sum" };
		;
		for (int i = 0; i < 18; i++) {
			lblResults[i] = new Label(names[i]);
			scorePane.add(lblResults[i], 0, i);

			txfResults[i] = new TextField();
			scorePane.add(txfResults[i], 1, i);
			txfResults[i].setEditable(false);
			txfResults[i].setMaxWidth(w);
		}

		// Todo:
		// Change color of SumSame, etc.

		scorePane.add(txfTotal, 3, 17);
		txfSumSame.setEditable(false);
		txfSumSame.setMaxWidth(w);

		Label lblTotal = new Label("Total");
		scorePane.add(lblTotal, 2, 17);

	}

	// -------------------------------------------------------------------------

	private class Controller {
		private YatzyDice dice = new YatzyDice();

		// Create a method for btnRoll's action.
		// Hint: Create small helper methods to be used in the action method.
		// TODO

		// -------------------------------------------------------------------------

		// Create a method for mouse click on one of the text fields in txfResults.
		// Hint: Create small helper methods to be used in the mouse click method.
		// TODO
	}

}
