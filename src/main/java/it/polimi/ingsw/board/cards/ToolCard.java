package it.polimi.ingsw.board.cards;

import it.polimi.ingsw.board.Game;
import it.polimi.ingsw.board.windowpattern.RestrictionEnum;

import java.io.Serializable;
import java.util.ArrayList;

public class ToolCard extends Card implements Serializable {
	private int id;
	private String name;
	private ArrayList<EffectsEnum> effectsEnums;
	private ArrayList<Effect> effects;
	private int favorTokensNumber;
//	private int totalSteps;
	private Game game;

	public ToolCard(int id, String name, ArrayList<EffectsEnum> effects) {
		this.id = id;
		this.name = name;
		this.favorTokensNumber = 0;
		if(effects != null) {
			this.effectsEnums = new ArrayList<>(effects);
			//populateEffects();
		}
		else
			this.effectsEnums = null;

	}
	public void reNew(){
		populateEffects();
	}
	public void populateEffects(){
		//totalSteps=0;
		effects = new ArrayList<>();
		for(int i=0;i<effectsEnums.size();i++){
		//	totalSteps++;
			switch(effectsEnums.get(i)){
				case SELECT_DICE_FROM_DRAFT:
					effects.add(new SelectDiceFromDraftEffect(game));
					break;
				case SELECT_DICE_FROM_WINDOW_PATTERN:
					effects.add(new SelectDiceFromWindowPatternEffect(game));
					break;
				case INCREMENT_DECREMENT_DICE:
					effects.add(new IncrementDecrementDiceEffect(game));
					break;
				case MOVE_WINDOW_PATTERN_DICE:
					if (id==2)
						effects.add(new MoveWindowPatternDiceEffect(game,RestrictionEnum.CELL_COLOR_RESTRICTION));
					else if(id==3)
						effects.add(new MoveWindowPatternDiceEffect(game,RestrictionEnum.CELL_VALUE_RESTRICTION));
					break;
				default:
					break;
			}
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getFavorTokensNumber() {
		return favorTokensNumber;
	}

	public void setFavorTokensNumber(int favorTokensNumber) {
		this.favorTokensNumber = favorTokensNumber;
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int validate(EffectsEnum effectsEnum){
		int i;
		for (i=0;effects.get(i).getMyEnum() != effectsEnum;i++) {
			if (!effects.get(i).isUsed()) {
				return -1;
			}
		}
		if (!effects.get(i).isUsed()){
			return i;
		}else
			return -1;
	}

	public Effect getNext(){
		int i;
		for (i=0;i < effects.size();i++) {
			if (!effects.get(i).isUsed()) {
				return effects.get(i);
			}
		}

		return null;
	}
}
