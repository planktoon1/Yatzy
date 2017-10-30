package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
	private final Label lblRolled = new Label("Rolled: 0");
	// private final Button btnRoll = new Button(" Roll ");

	private boolean throwing = true;

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

		dicePane.add(lblRolled, 5, 1);

		Button btnRoll = new Button("Roll");
		dicePane.add(btnRoll, 5, 0);
		btnRoll.setOnAction(event -> controller.newRollAction());

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
			if (i != 6 && i != 7 && i != 17) {
				txfResults[i].setOnMouseClicked(event -> controller.mouseClicked(event));
			}
			scorePane.add(txfResults[i], 1, i);
			txfResults[i].setEditable(false);
			txfResults[i].setMaxWidth(w);
		}

		txfResults[6].setUserData("selected");
		txfResults[17].setUserData("selected");
		// txfSumSame.setStyle("-fx-background-color: -#cee51c");
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

		public void newRollAction() {
			if (throwing) {
				boolean[] array = { cbxHolds[0].isSelected(), cbxHolds[1].isSelected(), cbxHolds[2].isSelected(),
						cbxHolds[3].isSelected(), cbxHolds[4].isSelected() };

				dice.throwDice(array);
				int s = 0;
				for (int i : dice.getValues()) {
					txfValues[s].setText("" + i);
					s++;
				}
				lblRolled.setText("Rolled: " + dice.getThrowCount());

				if (dice.getThrowCount() == 3) {
					throwing = false;
					fillPoints();
					dice.resetThrowCount();
				}
			}
		}

		private void mouseClicked(MouseEvent event) {
			if (!throwing) {
				TextField txf = (TextField) event.getSource();

				txf.setStyle("-fx-background-color: grey");
				txf.setUserData("selected");
				lblRolled.setText("Rolled: " + dice.getThrowCount());
				throwing = true;
				updateSum();
				resetResults();
			}
		}

		private void fillPoints() {

			if (txfResults[0].getUserData() != "selected") {
				txfResults[0].setText("" + dice.sameValuePoints(1));
			}
			if (txfResults[1].getUserData() != "selected") {
				txfResults[1].setText("" + dice.sameValuePoints(2));
			}
			if (txfResults[2].getUserData() != "selected") {
				txfResults[2].setText("" + dice.sameValuePoints(3));
			}
			if (txfResults[3].getUserData() != "selected") {
				txfResults[3].setText("" + dice.sameValuePoints(4));
			}
			if (txfResults[4].getUserData() != "selected") {
				txfResults[4].setText("" + dice.sameValuePoints(5));
			}
			if (txfResults[5].getUserData() != "selected") {
				txfResults[5].setText("" + dice.sameValuePoints(6));
			}
			if (txfResults[8].getUserData() != "selected") {
				txfResults[8].setText("" + dice.onePairPoints());
			}
			if (txfResults[9].getUserData() != "selected") {
				txfResults[9].setText("" + dice.twoPairPoints());
			}
			if (txfResults[10].getUserData() != "selected") {
				txfResults[10].setText("" + dice.threeSamePoints());
			}
			if (txfResults[11].getUserData() != "selected") {
				txfResults[11].setText("" + dice.fourSamePoints());
			}
			if (txfResults[12].getUserData() != "selected") {
				txfResults[12].setText("" + dice.fullHousePoints());
			}
			if (txfResults[13].getUserData() != "selected") {
				txfResults[13].setText("" + dice.smallStraightPoints());
			}
			if (txfResults[14].getUserData() != "selected") {
				txfResults[14].setText("" + dice.largeStraightPoints());
			}
			if (txfResults[15].getUserData() != "selected") {
				txfResults[15].setText("" + dice.chancePoints());
			}
			if (txfResults[16].getUserData() != "selected") {
				txfResults[16].setText("" + dice.yatzyPoints());
			}

			// if (sum >= 63) {
			// txfResults[7].setText("" + 50);
			//
			// } else
			// txfResults[7].setText("" + 0);

		}

		private void updateSum() {
			int sum = 0;
			for (int i = 0; i < 6; i++) {
				if (txfResults[i].getUserData() == "selected") {
					sum += Integer.parseInt(txfResults[i].getText());
				}
			}
			txfResults[6].setText("" + sum);
		}

		private void resetResults() {
			for (TextField t : txfResults) {
				if (t.getUserData() != "selected") {
					t.setText("" + 0);
				}
			}
		}
		// Create a method for btnRoll's action.
		// Hint: Create small helper methods to be used in the action method.
		// TODO

		// -------------------------------------------------------------------------

		// Create a method for mouse click on one of the text fields in txfResults.
		// Hint: Create small helper methods to be used in the mouse click method.
		// TODO
	}

}
