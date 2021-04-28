package edu.up.cs301.game.cheatymages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Arrays;
import edu.up.cs301.game.cheatymages.Cards.FighterCard;
import edu.up.cs301.game.cheatymages.Cards.JudgeCard;
import edu.up.cs301.game.cheatymages.Cards.SpellCard;
import edu.up.cs301.game.cheatymages.Players.CMHumanPlayer;

//TODO MAKE PAINTS FINAL
//TODO MAKE THE POSITIONS/SIZES FINAL INSTEAD OF HARD CODED

public class CMSurfaceView extends SurfaceView {

    private final float cardHeight = 240;
    private final float cardWidth = 180;

    private final float buttonHeight = 90;
    private final float buttonWidth = 120;

    private final float buttonX = 1650;
    private final float buttonY = 1800;
    private final float buttonYSpacing = 120;

    private final float fightersX = 50;
    private final float fightersY = 430;
    private final float fightersYSpacing = 300;

    private final float attachedSpellsXSpacing = 250;

    private final float judgeX = 15;
    private final float judgeY = 20;

    private final float handX = 20;
    private final float handY = 2000;
    private final float handXSpacing = 200;

    private final float labelRightMargin = 50;
    private final float labelY = 50;
    private final float labelYSpacing = 70;

    private final float labelX = 1200;
    private final float labelXSpacing = 1150;
    private final float xNumSpacing = 150;
    private final float titleSpacing = 850;

    private CMGameState state;
    private int playerId;
    private boolean[] fightersSelected;
    private boolean[] spellsSelected;
    private boolean[] fightersHighlighted;

    private final Paint roundInfoTextPaint = new Paint();
    private final Paint buttonLabelTextPaint = new Paint();
    private final Paint selectedCardPaint = new Paint();
    private final Paint fighterPowerPaint = new Paint();
    private final Paint goldSymbolPaint = new Paint();
    private final Paint fighterStatTextPaint = new Paint();
    private final Paint spellTypeSymbolPaint = new Paint();
    private final Paint manaTextPaint = new Paint();
    private final Paint spellEffectTextPaint = new Paint();
    private final Paint powerModTextPaint = new Paint();
    private final Paint manaLimitTextPaint = new Paint();
    private final Paint judgementTextPaint = new Paint();
    private final Paint cardOutlinePaint = new Paint();
    private final Paint cardBackFillPaint = new Paint();
    private final Paint cardBackTextPaint = new Paint();
    private final Paint titlePaint = new Paint();
    private final Paint buttonOutlinePaint = new Paint();
    private final Paint buttonBackgroundPaint = new Paint();
    private final Paint cardBackgroundPaint = new Paint();

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public CMSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);

        //makes the surface view background to transparent
        //code is adapted from https://stackoverflow.com/a/27959612
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        roundInfoTextPaint.setColor(Color.BLACK);
        roundInfoTextPaint.setTextSize(50);
        roundInfoTextPaint.setTextAlign(Paint.Align.RIGHT);

        selectedCardPaint.setColor(0x70FFD700);
        selectedCardPaint.setStyle(Paint.Style.STROKE);
        selectedCardPaint.setStrokeWidth(10.0f);

        fighterPowerPaint.setColor(Color.RED);
        goldSymbolPaint.setColor(0xFFFFD700);
        fighterStatTextPaint.setColor(Color.BLACK);
        fighterStatTextPaint.setTextSize(40);
        fighterStatTextPaint.setTextAlign(Paint.Align.CENTER);

        manaTextPaint.setColor(Color.BLACK);
        manaTextPaint.setTextAlign(Paint.Align.CENTER);
        manaTextPaint.setTextSize(40);
        manaTextPaint.setAntiAlias(true);

        spellEffectTextPaint.setColor(Color.BLACK);
        spellEffectTextPaint.setTextSize(25);
        spellEffectTextPaint.setAntiAlias(true);

        powerModTextPaint.setColor(Color.BLACK);
        powerModTextPaint.setTextSize(40.0f);
        powerModTextPaint.setAntiAlias(true);
        powerModTextPaint.setTextAlign(Paint.Align.CENTER);

        manaLimitTextPaint.setColor(Color.BLACK);
        manaLimitTextPaint.setTextAlign(Paint.Align.CENTER);
        manaLimitTextPaint.setTextSize(30.0f);
        manaLimitTextPaint.setAntiAlias(true);

        judgementTextPaint.setColor(Color.BLACK);
        judgementTextPaint.setTextSize(30.0f);
        judgementTextPaint.setTextAlign(Paint.Align.RIGHT);
        judgementTextPaint.setAntiAlias(true);

        cardOutlinePaint.setColor(Color.BLACK);
        cardOutlinePaint.setStyle(Paint.Style.STROKE);
        cardOutlinePaint.setStrokeWidth(10.0f);

        cardBackTextPaint.setColor(Color.BLACK);
        cardBackTextPaint.setTextAlign(Paint.Align.CENTER);
        cardBackTextPaint.setTextSize(45.0f);
        cardBackTextPaint.setAntiAlias(true);
        cardBackgroundPaint.setColor(Color.WHITE);

        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(35.0f);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);

        buttonOutlinePaint.setColor(Color.BLACK);
        buttonOutlinePaint.setStyle(Paint.Style.STROKE);
        buttonOutlinePaint.setStrokeWidth(10.0f);
        buttonBackgroundPaint.setColor(Color.WHITE);

        buttonLabelTextPaint.setColor(Color.BLACK);
        buttonLabelTextPaint.setTextSize(30);
        buttonLabelTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setState(CMGameState state, int playerId){
        this.state = state;
        fightersSelected = new boolean[5];
        fightersHighlighted = new boolean[5];
        if(state.getNumPlayers() > 4) {
            spellsSelected = new boolean[6];
        }
        else{
            spellsSelected = new boolean[8];
        }
        this.playerId = playerId;
    }

    public void selectFighter(int idx, boolean selected){
        fightersSelected[idx] = selected;
    }

    public void selectSpell(int idx, boolean selected){
        spellsSelected[idx] = selected;
    }

    public void clearFighterSelections(){
        Arrays.fill(fightersSelected, false);
    }

    public void clearSpellSelections(){
        Arrays.fill(spellsSelected, false);
    }

    /* TODO IMPLEMENT THIS SO THAT THE DETECT MAGIC BUTTON IS HIGHLIGHTED
    public void selectDetectMagic(){

    }
     */

    /**
     * onDraw Method draws everything on the screen
     * @param canvas
     */
    protected void onDraw(Canvas canvas){

        if(state == null){
            return;
        }

        drawRandomJudge(canvas);

        drawRandomFighters(canvas);

        drawPlayerHand(canvas);

        drawPlayedSpellCards(canvas);

        drawActionButtons(canvas);

        drawLabels(canvas);

        drawOpponentTable(canvas);

        invalidate();
    }

    /**
     * drawRandomJudge is a helper method that will draw the random judge from CMGameState
     * @param canvas
     */
    public void drawRandomJudge(Canvas canvas) {
        // Determines judges judgement
        //TODO CHANGE THIS
        String judgementType;
        if(state.getJudge().getJudgementType() == 'd') {
            judgementType = "Dispel";
        }
        else {
            judgementType = "Eject";
        }
        // Draws judge card
        JudgeCard judge = state.getJudge();
        drawJudgeCard(canvas, judgeX, judgeY, judge.getName(), judge.getManaLimit(),
                judgementType, judge.getDisallowedSpells(), judge.getName().equals("Tad"));
    }

    /**
     * drawRandomFighter is a helper method that draws the random 5 fighter cards from CMGameState
     * @param canvas
     */
    public void drawRandomFighters(Canvas canvas) {
        // Draws 5 random fighter cards
        if(state.getPlayerTurn() == -1){
            fightersHighlighted = fightersSelected;
        }
        else{
            ArrayList<Integer> bets = state.getBets()[playerId];
            for(int i = 0; i < bets.size(); i++){
                fightersHighlighted[bets.get(i)] = true;
            }
        }
        for(int i=0; i < 5; i++) {
            FighterCard fighter = state.getFighter(i);
            drawFighterCard(canvas, fightersX, fightersY + fightersYSpacing*i, fighter.getName(),
                    fighter.getPower(), fighter.getPrizeMoney(), fightersHighlighted[i]);
        }
    }

    /**
     * drawPlayerHand is a helper method that draws the users hand of spell cards on the screen
     * @param canvas
     */
    public void drawPlayerHand(Canvas canvas){
        // Draws players hand of spell cards
        for(int i=0; i < state.getHands()[playerId].size(); i++) {
            SpellCard spell = state.getHands()[playerId].get(i);
            drawSpellCard(canvas, handX + handXSpacing*i, handY, spell.getName(), spell.getMana(),
                    spell.getSpellType(), spell.getPowerMod(),false, "",
                    spell.isForbidden(), spellsSelected[i]);
        }
    }

    /**
     * drawPlayedSpellCards is a helper method that draws the played spell cards to next
     * to the fighter card played on
     * @param canvas
     */
    public void drawPlayedSpellCards(Canvas canvas) {
        // Draws played spell cards
        for(int i = 0; i < 5; i++){
            for(int j=0; j<state.getAttachedSpells()[i].size(); j++) {
                SpellCard spell = state.getAttachedSpells()[i].get(j);
                if(spell.getSpellType() == ' ') {
                    drawFaceDownCard(canvas, fightersX + attachedSpellsXSpacing*(j+1),
                            fightersY + fightersYSpacing*i, Color.GRAY);
                }
                else {
                    drawSpellCard(canvas, fightersX + attachedSpellsXSpacing*(j+1),
                            fightersY + fightersYSpacing*i, spell.getName(), spell.getMana(),
                            spell.getSpellType(), spell.getPowerMod(), false, "",
                            spell.isForbidden(), false);
                }
            }
        }
    }

    /**
     * drawLabels is a helper method that draws the round, phase/turn, and gold total on screen
     * @param canvas
     */
    public void drawLabels(Canvas canvas){
        // draws labels
        canvas.drawText("Round " + state.getRoundNum(), getWidth() - labelRightMargin,
                labelY, roundInfoTextPaint);
        String turnText = "";
        if(state.getPlayerTurn() >= 0){
            turnText = "Turn " + (state.getPlayerTurn());
        }
        else if(state.getPlayerTurn() == -1){
            turnText = "Betting Phase";
        }
        else if(state.getPlayerTurn() == -2){
            turnText = "Discarding Phase";
        }
        canvas.drawText(turnText, getWidth() - labelRightMargin,
                labelY + labelYSpacing, roundInfoTextPaint);
        canvas.drawText("Gold: " + Integer.toString(state.getGold()[playerId]),
                getWidth() - labelRightMargin, labelY + labelYSpacing*2, roundInfoTextPaint);

    }

    public void drawOpponentTable(Canvas canvas){
        // draws other players, gold, bets, and hand size label
        canvas.drawText("Other Players", getWidth() - titleSpacing, labelY, roundInfoTextPaint);
        canvas.drawText("Gold:", getWidth()-labelXSpacing, labelY + (2*labelYSpacing), roundInfoTextPaint);
        canvas.drawText("Bets:", getWidth()-labelXSpacing, labelY + (3*labelYSpacing), roundInfoTextPaint);
        canvas.drawText("Hand Size:", getWidth()-labelXSpacing, labelY + (4*labelYSpacing), roundInfoTextPaint);
        // draws all player numbers, total gold, total bets, and hand size
        for(int i = 1; i < state.getNumPlayers(); i++) {
            canvas.drawText("P" + i, getWidth() - labelX + (xNumSpacing*i), labelY + labelYSpacing, roundInfoTextPaint);
            canvas.drawText(String.valueOf(state.getGold()[i]), getWidth() - labelX + (xNumSpacing*i), labelY + (2*labelYSpacing), roundInfoTextPaint);
            canvas.drawText(String.valueOf(state.getBets()[i].size()), getWidth() - labelX + (xNumSpacing*i), labelY + (3*labelYSpacing), roundInfoTextPaint);
            canvas.drawText(String.valueOf(state.getHands()[i].size()), getWidth() - labelX + (xNumSpacing*i), labelY + (4*labelYSpacing), roundInfoTextPaint);
        }
    }

    /**
     * drawActionButtons is a helper method that draws the Action buttons (PASS, DETECT MAGIC,
     * BET, AND DISCARD) on screen
     * @param canvas
     */
    public void drawActionButtons(Canvas canvas){
        // draws buttons
        float buttonYPos = buttonY;
        drawButton(canvas, buttonX, buttonYPos, "Pass");
        buttonYPos += buttonYSpacing;
        drawButton(canvas, buttonX, buttonYPos, "Detect");
        buttonYPos += buttonYSpacing;
        drawButton(canvas, buttonX, buttonYPos, "Bet");
        buttonYPos += buttonYSpacing;
        drawButton(canvas, buttonX, buttonYPos, "Discard");
        buttonYPos += buttonYSpacing;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public String mapPositionToItem(int x, int y){
        //TODO CHANGE THIS FUNCTION TO WORK WITH CHANGING CARD SPACING
        if(fightersX <= x && x <= fightersX + cardWidth){
            for(int i = 0; i < 5; i++){
                if(fightersY + fightersYSpacing*i <= y && y <= fightersY + fightersYSpacing*i + cardHeight){
                    return "Fighter " + (i + 1);
                }
            }
        }
        if(handY <= y && y <= handY + cardHeight){
            for(int i = 0; i < 8; i++){
                if(handX + handXSpacing*i <= x && x <= handX + handXSpacing*i + cardWidth){
                    return "Spell " + (i + 1);
                }
            }
        }

        float buttonXPos = buttonX;
        float buttonYPos = buttonY;
        if(buttonXPos <= x && x <= buttonXPos + buttonWidth && buttonYPos <= y && y <= buttonYPos + buttonHeight){
            return "Pass";
        }
        buttonYPos += buttonYSpacing;
        if(buttonXPos <= x && x <= buttonXPos + buttonWidth && buttonYPos <= y && y <= buttonYPos + buttonHeight){
            return "Detect Magic";
        }
        buttonYPos += buttonYSpacing;
        if(buttonXPos <= x && x <= buttonXPos + buttonWidth && buttonYPos <= y && y <= buttonYPos + buttonHeight){
            return "Bet";
        }
        buttonYPos += buttonYSpacing;
        if(buttonXPos <= x && x <= buttonXPos + buttonWidth && buttonYPos <= y && y <= buttonYPos + buttonHeight){
            return "Discard";
        }
        return "";
    }

    /**
     * drawFighterCard method draws a fighter card on the screen
     *
     * @param canvas
     * @param x top left corner of the card
     * @param y top left corner of the card
     * @param fighterName fighters name
     * @param power fighters power level
     * @param prizeGold fighters prize money
     * @param hasBet of you bet on that fighter card
     */
    protected void drawFighterCard(Canvas canvas, float x, float y, String fighterName,
                                   int power, int prizeGold, boolean hasBet){

        //draws a yellow translucent border around the card if you've placed a bet on it
        if(hasBet){
            canvas.drawRect(x - 10.0f, y - 10.0f, x + cardWidth + 10.0f,
                    y + cardHeight + 10.0f, selectedCardPaint);
        }
        //Draws white card background
        canvas.drawRect(x, y, x + cardWidth,
                y + cardHeight, cardBackgroundPaint);

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, fighterName);

        //draws the circle for the fighter's power
        canvas.drawCircle(x + 40.0f, y + cardHeight - 40.0f, 25.0f, fighterPowerPaint);

        //draws the circle for the fighter's prize gold
        canvas.drawCircle(x + cardWidth - 40.0f, y + cardHeight - 40.0f, 25.0f,
                goldSymbolPaint);

        //draws the text for the fighter's stats
        canvas.drawText(Integer.toString(power), x + 40.0f, y + cardHeight - 25.0f,
                fighterStatTextPaint);
        canvas.drawText(Integer.toString(prizeGold), x + cardWidth - 40.0f,
                y + cardHeight - 25.0f, fighterStatTextPaint);

    }

    /**
     * draws a spell card on the screen
     * if hasCardText is true the card is printed with textEffect at the bottom
     * else if hasCardText is false the card only has a power modifier
     *
     * @param canvas
     * @param x top left corner of the card
     * @param y top left corner of the card
     * @param spellName Name of the spell card
     * @param mana amount of mana on card
     * @param spellType  spell types: d = direct, e = enchant, s = support
     * @param powerMod power modification on card
     * @param hasCardText cards text
     * @param effectText explains the effect of the card
     * @param isForbidden checks if it is a forbidden type spell
     */
    protected void drawSpellCard(Canvas canvas, float x, float y, String spellName, int mana,
                                 char spellType, int powerMod, boolean hasCardText,
                                 String effectText, boolean isForbidden, boolean hasSelected){

        if(hasSelected){
            canvas.drawRect(x - 10.0f, y - 10.0f, x + cardWidth + 10.0f,
                    y + cardHeight + 10.0f, selectedCardPaint);
        }
        //Draws white card background
        canvas.drawRect(x, y, x + cardWidth,
                y + cardHeight, cardBackgroundPaint);

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, spellName);

        //draws a symbol according to the spell's type
        //colored circles are used as stand ins for now
        if(spellType == 'd') {
            spellTypeSymbolPaint.setColor(Color.RED);
        }
        else if(spellType == 'e') {
            spellTypeSymbolPaint.setColor(Color.BLUE);
        }
        else {
            spellTypeSymbolPaint.setColor(0xFFFFD700);
        }

        canvas.drawCircle(x + 25.0f, y + 75.0f, 12.5f, spellTypeSymbolPaint);

        //draws mana cost on the card
        if(mana != 0) {
            canvas.drawText(Integer.toString(mana), x + cardWidth - 30.0f, y + 90.0f, manaTextPaint);
        }

        //draws a forbidden icon (green circle place holder for now) if the card is forbidden
        spellTypeSymbolPaint.setColor(Color.GREEN);
        if(isForbidden){
            canvas.drawCircle(x + 25.0f, y + 95.0f, 12.5f, spellTypeSymbolPaint);
        }

        //draws the effect text at the bottom of the card if needed
        if(hasCardText){
            ArrayList<String> wrappedEffectText = textLineWrap(effectText, cardWidth - 30.0f,
                    spellEffectTextPaint);
            for(int i = 0; i < wrappedEffectText.size(); i++){
                canvas.drawText(wrappedEffectText.get(i), x + 10.0f,
                        y + cardHeight - 80.0f + i * 22.5f, spellEffectTextPaint);
            }
        }

        //draws the power modifier on the bottom of the card if no card text is present
        else{
            //adds a plus or minus to the front of the modifier
            String powerModText = "";
            if(powerMod > 0){
                powerModText = "+";
            }
            powerModText += Integer.toString(powerMod);
            canvas.drawText(powerModText, x + cardWidth/2, y + cardHeight - 25.0f,
                    powerModTextPaint);
        }

    }

    /**
     * draws a judge card on the screen
     *
     * @param canvas
     * @param x top left corner of the card
     * @param y top left corner of the card
     * @param judgeName name of Judge
     * @param manaLimit Judges mana limit
     * @param judgementType Judges judgement (Either Eject or Dispel)
     * @param disallows disallows shows which card types this judge bans (In order: direct, enchant, support, forbidden)
     */
    protected void drawJudgeCard(Canvas canvas, float x, float y, String judgeName, int manaLimit,
                                 String judgementType, ArrayList<Character> disallows,
                                 boolean noManaLimit) {

        //Draws white card background
        canvas.drawRect(x, y, x + cardWidth,
                y + cardHeight, cardBackgroundPaint);

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, judgeName);

        if(!noManaLimit) {
            //writes the mana limit on the card
            canvas.drawText(Integer.toString(manaLimit), x + 30.0f, y + cardHeight - 75.0f,
                    manaLimitTextPaint);

            //writes judgement type on the card
            canvas.drawText(judgementType, x + cardWidth - 20.0f,y + cardHeight - 75.0f,
                    judgementTextPaint);

        }

        //draws symbols of cards this judge disallows at bottom of card
        for(int i = 0; i < disallows.size(); i++){
            if (disallows.get(i) == 'd') {
                spellTypeSymbolPaint.setColor(Color.RED);
            } else if (disallows.get(i) == 'e') {
                spellTypeSymbolPaint.setColor(Color.BLUE);
            } else if (disallows.get(i) == 's') {
                spellTypeSymbolPaint.setColor(0xFFFFD700);
            } else {
                spellTypeSymbolPaint.setColor(Color.GREEN);
            }
            canvas.drawCircle(x + 25.0f + i * 25.0f, y + cardHeight - 40.0f,
                    12.5f, spellTypeSymbolPaint);
        }

    }

    /**
     * draws a face down card on the screen
     *
     * @param canvas
     * @param x top left corner of the card
     * @param y top left corner of the card
     * @param cardBackColor color of card
     */
    protected void drawFaceDownCard(Canvas canvas, float x, float y, int cardBackColor){
        //draws the card back's base color first
        cardBackFillPaint.setColor(cardBackColor);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, cardBackFillPaint);

        //draws the outline of the card
        drawCardOutline(canvas, x, y);

        //prints the "cheaty mages" across the card
        canvas.drawText("CHEATY", x + cardWidth/2, y + 110.0f, cardBackTextPaint);
        canvas.drawText("MAGES", x + cardWidth/2, y + cardHeight - 70.0f, cardBackTextPaint);
    }

    /**
     * draws the black border for the card outline
     *
     * @param canvas
     * @param x top left corner of the outline
     * @param y top left corner of the outline
     */
    protected void drawCardOutline(Canvas canvas, float x, float y){
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, cardOutlinePaint);
    }

    /**
     * draws the card name at the top of the card
     *
     * @param canvas
     * @param x top left corner of the text(title)
     * @param y top left corner of the text(title)
     * @param text the card title
     */
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        canvas.drawText(text, x + cardWidth/2, y + 50.0f, titlePaint);
    }

    /**
     * converts a string into an array of smaller strings using a max pixel width
     * this is intended be used to wrap text within a certain width
     * this code is functional for now but will cut off words and will need to be altered
     *
     * @param text
     * @param width
     * @param textPaint
     * @return
     */
    protected ArrayList<String> textLineWrap(String text, float width, Paint textPaint){
        Rect bounds = new Rect();
        ArrayList<String> splitText = new ArrayList<String>();
        int textStart = 0;
        for (int i = 0; i <= text.length(); i++) {
            textPaint.getTextBounds(text, textStart, i, bounds);
            if(bounds.width() > width) {
                splitText.add(text.substring(textStart, i));
                textStart = i;
            }
        }
        splitText.add(text.substring(textStart));
        return splitText;
    }

    protected void drawButton(Canvas canvas, float x, float y, String label){
        canvas.drawRect(x, y, x + buttonWidth, y + buttonHeight, buttonBackgroundPaint);
        canvas.drawRect(x, y, x + buttonWidth, y + buttonHeight, buttonOutlinePaint);
        canvas.drawText(label, x + buttonWidth/2, y + buttonHeight/2, buttonLabelTextPaint);
    }
}
