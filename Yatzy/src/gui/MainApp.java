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

	private final Label lblRolled = new Label("Rolled: 0");

	// private final Button btnRoll = new Button(" Roll ");

	// Whether or not we have more throws left (3 throws per. turn)
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
		dicePane.setStyle("-fx-background-color: #8ABD5F;");
		pane.setStyle("-fx-background-color: #609732");

		for (int i = 0; i < 5; i++) {
			txfValues[i] = new TextField();
			dicePane.add(txfValues[i], i, 0);
			txfValues[i].setEditable(false);
			txfValues[i].setStyle("-fx-font-size: 26; -fx-border-color: #B9E397; -fx-border-width: 2");
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
		scorePane.setStyle("-fx-background-color: #8ABD5F");
		int w = 50; // width of the text fields

		String[] names = { "1'ere", "2'ere", "3'ere", "4'ere", "5'ere", "6'ere", "Sum", "Bonus", "1 par", "2 par",
				"3 ens", "4 ens", "Fuldt hus", "Small straight", "Large straight", "Chance", "Yatzy", "Sum" };
		;
		for (int i = 0; i < 18; i++) {
			lblResults[i] = new Label(names[i]);
			scorePane.add(lblResults[i], 0, i);
			lblResults[i].setStyle("-fx-font-weight: bold");

			txfResults[i] = new TextField();
			if (i != 6 && i != 7 && i != 17) {
				txfResults[i].setOnMouseClicked(event -> controller.mouseClicked(event));
			}
			scorePane.add(txfResults[i], 1, i);
			txfResults[i].setEditable(false);
			txfResults[i].setMaxWidth(w);

		}

		txfResults[6].setUserData("selected");
		txfResults[7].setUserData("selected");
		txfResults[17].setUserData("selected");

		lblResults[6].setStyle("-fx-text-fill: red; -fx-font-weight: bold");
		lblResults[7].setStyle("-fx-text-fill: red; -fx-font-weight: bold");
		lblResults[17].setStyle("-fx-text-fill: red; -fx-font-weight: bold");

		scorePane.add(txfTotal, 3, 17);
		txfSumSame.setEditable(false);
		txfSumSame.setMaxWidth(w);

		Label lblTotal = new Label("Total");
		lblTotal.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
		scorePane.add(lblTotal, 2, 17);

	}

	// -------------------------------------------------------------------------

	private class Controller {
		private YatzyDice dice = new YatzyDice();

		/**
		 * Throws the dice that is not held updates the corrosponding textfields and
		 * increments the lblRolled pre: Throwcount is < 3; (Throwing is a boolean
		 * checking for this)
		 */
		public void newRollAction() {
			if (throwing) {
				enableCheckBox();
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

		/**
		 * Selects the clicket textfield. Selected fields are the final score for it's
		 * category. See method comment on the helpmethods for futher explanation pre:
		 * throwcount = 3;
		 */
		private void mouseClicked(MouseEvent event) {
			if (!throwing) {

				TextField txf = (TextField) event.getSource();
				txf.setStyle("-fx-background-color: #B9E397");
				txf.setUserData("selected");
				lblRolled.setText("Rolled " + dice.getThrowCount());
				throwing = true;
				updateSum();
				updateBonus();
				resetResults();
				updateTotal();
				disableCheckBox();
				resetCheckBox();

			}
		}

		/**
		 * Fills all the non selected textFields with the calculated points.
		 */
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
		}

		/**
		 * Checks if bonus should be added, and adds the bonus to the bonus textfield
		 * pre: txfResults[6].getText() != null;
		 */
		private void updateBonus() {
			if (Integer.parseInt(txfResults[6].getText()) >= 63) {
				txfResults[7].setText("" + 50);
			} else
				txfResults[7].setText("" + 0);

		}

		/**
		 * Calculates the two sum values and inserts them into the sum textfields. pre:
		 * txfResults index 0-5 and 8-16 != null,
		 */
		private void updateSum() {
			int sumSame = 0;
			for (int i = 0; i < 6; i++) {
				if (txfResults[i].getUserData() == "selected") {
					sumSame += Integer.parseInt(txfResults[i].getText());
				}
			}
			txfResults[6].setText("" + sumSame);

			int sumOther = 0;
			for (int i = 8; i < 17; i++) {
				if (txfResults[i].getUserData() == "selected") {
					sumOther += Integer.parseInt(txfResults[i].getText());
				}
			}
			txfResults[17].setText("" + sumOther);
		}

		/**
		 * Calculates the total points and adds it to the total textfield
		 */
		private void updateTotal() {
			int Total = Integer.parseInt(txfResults[6].getText()) + Integer.parseInt(txfResults[7].getText())
					+ Integer.parseInt(txfResults[17].getText());
			txfTotal.setText("" + Total);
		}

		/**
		 * Sets all the non selected textfields to 0
		 */
		private void resetResults() {
			for (TextField t : txfResults) {
				if (t.getUserData() != "selected") {
					t.setText("" + 0);
				}
			}
		}

		/**
		 * Enables all the the checkboxes that determines if a dice is held or not
		 */
		private void enableCheckBox() {
			for (int i = 0; i < 5; i++)
				cbxHolds[i].setDisable(false);

		}

		/**
		 * Disables all the the checkboxes that determines if a dice is held or not
		 */
		private void disableCheckBox() {
			for (int i = 0; i < 5; i++)
				cbxHolds[i].setDisable(true);

		}

		/**
		 * Sets all the the checkboxes that determines if a dice is held or not to 0
		 */
		private void resetCheckBox() {
			for (int i = 0; i < 5; i++)
				cbxHolds[i].setSelected(false);
		}

		// ere
		/**
		 * Checks if the game is over, and ends the game if it is.
		 */
		private void endgame() {
			boolean over = true;

			for (TextField t : txfResults) {
				if (t.getUserData() != "selected") {
					over = false;
				}
				if (over = true) {

				}
			}
		}
	}
}