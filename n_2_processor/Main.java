package n_2_processor;

import java.util.List;

import n_2_board.Board;
import n_2_board.BoardOfPlayer;
import n_2_board.Player;
import n_2_card.C_P_E_DepartmentStore;
import n_2_card.C_P_E_Expo;
import n_2_card.C_P_E_Stall;
import n_2_card.C_P_E_Supermarket;
import n_2_card.C_P_E_market;
import n_2_card.C_P_HighSchool;
import n_2_card.C_P_University;
import n_2_card.C_P_Vocational;
import n_2_card.Card;

public class Main {

	public static void main(String[] args) {
		Board board = new Board();
		Player player = new Player(board);
		P processor = new P();
		FacilityCalc faciCalc = new FacilityCalc();

		int turn = 1;
		while(turn < 10) {

//			追加ボード
			switch(turn) {
				case 2:
					board.getCards().add(new C_P_E_Stall());
					break;
				case 3:
					board.getCards().add(new C_P_E_market());
					break;
				case 4:
					board.getCards().add(new C_P_HighSchool());
					break;
				case 5:
					board.getCards().add(new C_P_E_Supermarket());
					break;
				case 6:
					board.getCards().add(new C_P_University());
					break;
				case 7:
					board.getCards().add(new C_P_E_DepartmentStore());
					break;
				case 8:
					board.getCards().add(new C_P_Vocational());
					break;
				case 9:
					board.getCards().add(new C_P_E_Expo());
					break;
			}
//			各自省略
			List<Card> pc = player.getCards();
			BoardOfPlayer pb = player.getBoardOfPlayer();
			List<Card> bc = board.getCards();

			for (int i = 0; i < player.getWokers().size(); i++) {
				player.getWokers().get(i).setOn(true);;
			}
			System.out.println(turn+"ターン目");
			boolean isWoker = true;
			int count = 1;
			int wokerUse = 0;
			while(count>0) {
				count = 0;
//				for (int j = 1; j <= player.getWokers().size(); j++) {
				for (int i = 0; i < player.getWokers().size(); i++) {
					if(player.getWokers().get(i).isOn()) {
//						System.out.println(player.getWokers().get(i).isOn());
						count++;
					}
				}
				if(wokerUse < player.getWokers().size()) {
					System.out.println(wokerUse+1+"目の労働者");
					System.out.println("");
					player.getWokers().get(wokerUse).setOn(player.useboard(board, processor));
				}
//				施設の計算
				faciCalc.facilityCalc(player);
				wokerUse++;
//				System.out.println("wokerUse="+wokerUse);
				for (int j = 0; j < player.getBoardOfPlayer().getCards().size(); j++) {
					List<Card> checkBurns = player.getBoardOfPlayer().getCards();
					if(checkBurns.get(j).getName().equals("焼畑")) {
						if(!checkBurns.get(j).isOn()) {
							board.plusTrashCard(checkBurns.remove(j));
						}
					}
				}
			}
			for (int i = 0; i < board.getTrashCard().getCards().size(); i++) {
				if(board.getTrashCard().getCards().get(i).getType().equals("0")) {
					board.getTrashCard().getCards().remove(i);
				}
			}
			WokerCalc.W_calc(board, player);
//			プレイヤーボードのカードを使える状態の戻す（施設以外）
			for (int j = 0; j < player.getBoardOfPlayer().getCards().size(); j++) {
				if(!player.getBoardOfPlayer().getCards().get(j).getType().equals("F"))
				player.getBoardOfPlayer().getCards().get(j).setOn(true);
			}
//			ボードのカードを使える状態の戻す
			for (int j = 0; j < board.getCards().size(); j++) {
				board.getCards().get(j).setOn(true);
			}
//			手札を制限数に揃える
			if(pc.size()>player.getHandlimit()) {
				while(pc.size()>player.getHandlimit()) {
					System.out.println(pc.size());
					System.out.println(player.getHandlimit());
					M.CardChoiceTrash(pc.size() - player.getHandlimit());
					int trashcard = P.SelectFromPlayerCard(player, board);
					board.plusTrashCard(pc.remove(trashcard));
				}
			}
//			給料支払い
			MoneyCalc moneyCalc = new MoneyCalc();
			moneyCalc.moneyCalc(player, board, turn);
			turn++;
		}
	}

}
