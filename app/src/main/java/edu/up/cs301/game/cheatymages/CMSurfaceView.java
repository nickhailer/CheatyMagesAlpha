package edu.up.cs301.game.cheatymages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import edu.up.cs301.game.cheatymages.Cards.JudgeCard;
import edu.up.cs301.game.cheatymages.Cards.SpellCard;
import edu.up.cs301.game.cheatymages.Players.CMHumanPlayer;

public class CMSurfaceView extends SurfaceView {

    protected final float cardHeight = 240;
    protected final float cardWidth = 180;
    protected CMGameState state;
    protected int playerId;
    protected boolean[] fightersSelected;
    protected boolean[] spellsSelected;

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
    }

    public void setState(CMGameState state, int playerId){
        this.state = state;
        fightersSelected = new boolean[5];
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
        
        // Determines judges judgement
        String judgementType;
        if(state.getJudge().getJudgementType() == 'd') {
            judgementType = "Dispel";
        }
        else {
            judgementType = "Eject";
        }
        // Draws judge card
        JudgeCard judge = state.getJudge();
        drawJudgeCard(canvas, 15, 120, judge.getName(), judge.getManaLimit(),
                        judgementType, judge.getDisallowedSpells(), judge.getName().equals("Tad"));

        // Draws 5 random fighter cards
        for(int i=0; i < 5; i++) {
            drawFighterCard(canvas, 50, 430 + (300*i), state.getFighter(i).getName(),
                    state.getFighter(i).getPower(), state.getFighter(i).getPrizeMoney(), false);
        }

        // Draws players hand of spell cards
        for(int i=0; i < state.getHands()[playerId].size(); i++) {
            SpellCard spell = state.getHands()[playerId].get(i);
            drawSpellCard(canvas, 20 + (210*i), 2000, spell.getName(), spell.getMana(),
                    spell.getSpellType(), spell.getPowerMod(),false, "",
                    spell.isForbidden(), spellsSelected[i]);
        }

        // Draws played spell cards
        for(int i = 0; i < 5; i++){
            int yCoord = 430 + i*300;
            for(int j=0; j<state.getAttachedSpells()[i].size(); j++) {
                SpellCard spell = state.getAttachedSpells()[i].get(i);
                if(spell.getSpellType() == 'e') {
                    drawFaceDownCard(canvas, 300 + (250*i), yCoord, Color.GRAY);
                }
                else {
                    drawSpellCard(canvas, 300 + (250*i), yCoord, spell.getName(), spell.getMana(),
                            spell.getSpellType(), spell.getPowerMod(), false, "",
                            spell.isForbidden(), false);
                }
            }
        }

        // draws coin label
        drawCoin(canvas, 1400, -45, "C");

        invalidate();
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
            Paint outlinePaint = new Paint();
            outlinePaint.setColor(0x70FFD700);
            outlinePaint.setStyle(Paint.Style.STROKE);
            outlinePaint.setStrokeWidth(10.0f);
            canvas.drawRect(x - 10.0f, y - 10.0f, x + cardWidth + 10.0f, y + cardHeight + 10.0f,
                    outlinePaint);
        }

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, fighterName);

        //draws the circle for the fighter's power
        Paint red = new Paint();
        red.setColor(Color.RED);
        canvas.drawCircle(x + 40.0f, y + cardHeight - 40.0f, 25.0f, red);

        //draws the circle for the fighter's prize gold
        Paint gold = new Paint();
        gold.setColor(0xFFFFD700);
        canvas.drawCircle(x + cardWidth - 40.0f, y + cardHeight - 40.0f, 25.0f, gold);

        //draws the text for the fighter's stats
        Paint statText = new Paint();
        statText.setColor(Color.BLACK);
        statText.setTextSize(40);
        statText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Integer.toString(power), x + 40.0f, y + cardHeight - 25.0f, statText);
        canvas.drawText(Integer.toString(prizeGold), x + cardWidth - 40.0f, y + cardHeight - 25.0f, statText);

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
            Paint outlinePaint = new Paint();
            outlinePaint.setColor(0x70FFD700);
            outlinePaint.setStyle(Paint.Style.STROKE);
            outlinePaint.setStrokeWidth(10.0f);
            canvas.drawRect(x - 10.0f, y - 10.0f, x + cardWidth + 10.0f, y + cardHeight + 10.0f,
                    outlinePaint);
        }

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, spellName);

        //draws a symbol according to the spell's type
        //colored circles are used as stand ins for now
        Paint spellTypeSymbolPaint = new Paint();
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
            Paint manaTextPaint = new Paint();
            manaTextPaint.setColor(Color.BLACK);
            manaTextPaint.setTextAlign(Paint.Align.CENTER);
            manaTextPaint.setTextSize(40);
            manaTextPaint.setAntiAlias(true);
            canvas.drawText(Integer.toString(mana), x + cardWidth - 30.0f, y + 90.0f, manaTextPaint);
        }

        //draws a forbidden icon (green circle place holder for now) if the card is forbidden
        if(isForbidden){
            Paint forbiddenSymbolPaint = new Paint();
            forbiddenSymbolPaint.setColor(Color.GREEN);
            canvas.drawCircle(x + 25.0f, y + 95.0f, 12.5f, forbiddenSymbolPaint);
        }

        //draws the effect text at the bottom of the card if needed
        if(hasCardText){
            Paint effectTextPaint = new Paint();
            effectTextPaint.setColor(Color.BLACK);
            effectTextPaint.setTextSize(25);
            effectTextPaint.setAntiAlias(true);
            ArrayList<String> wrappedEffectText = textLineWrap(effectText, cardWidth - 30.0f,
                    effectTextPaint);
            for(int i = 0; i < wrappedEffectText.size(); i++){
                canvas.drawText(wrappedEffectText.get(i), x + 10.0f, y + cardHeight - 80.0f + i * 22.5f,
                        effectTextPaint);
            }
        }

        //draws the power modifier on the bottom of the card if no card text is present
        else{
            Paint powerModTextPaint = new Paint();
            powerModTextPaint.setColor(Color.BLACK);
            powerModTextPaint.setTextSize(40.0f);
            powerModTextPaint.setAntiAlias(true);
            powerModTextPaint.setTextAlign(Paint.Align.CENTER);

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

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, judgeName);

        if(!noManaLimit) {
            //writes the mana limit on the card
            Paint manaLimitTextPaint = new Paint();
            manaLimitTextPaint.setColor(Color.BLACK);
            manaLimitTextPaint.setTextAlign(Paint.Align.CENTER);
            manaLimitTextPaint.setTextSize(30.0f);
            manaLimitTextPaint.setAntiAlias(true);
            canvas.drawText(Integer.toString(manaLimit), x + 30.0f, y + cardHeight - 75.0f,
                    manaLimitTextPaint);

            //writes judgement type on the card
            Paint judgementTextPaint = new Paint();
            judgementTextPaint.setColor(Color.BLACK);
            judgementTextPaint.setTextSize(30.0f);
            judgementTextPaint.setTextAlign(Paint.Align.RIGHT);
            judgementTextPaint.setAntiAlias(true);
            canvas.drawText(judgementType, x + cardWidth - 90.0f,y + cardHeight - 75.0f,
                    judgementTextPaint);

        }

        //draws symbols of cards this judge disallows at bottom of card
        Paint symbolPaint = new Paint();
        for(int i = 0; i < disallows.size(); i++){
            if (disallows.get(i) == 'd') {
                symbolPaint.setColor(Color.RED);
            } else if (disallows.get(i) == 'e') {
                symbolPaint.setColor(Color.BLUE);
            } else if (disallows.get(i) == 's') {
                symbolPaint.setColor(0xFFFFD700);
            } else {
                symbolPaint.setColor(Color.GREEN);
            }
            canvas.drawCircle(x + 25.0f + i * 25.0f, y + cardHeight - 40.0f,
                    12.5f, symbolPaint);
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
        Paint cardBackFillPaint = new Paint();
        cardBackFillPaint.setColor(cardBackColor);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, cardBackFillPaint);

        //draws the outline of the card
        drawCardOutline(canvas, x, y);

        //prints the "cheaty mages" across the card
        Paint cardBackTextPaint = new Paint();
        cardBackTextPaint.setColor(Color.BLACK);
        cardBackTextPaint.setTextAlign(Paint.Align.CENTER);
        cardBackTextPaint.setTextSize(45.0f);
        cardBackTextPaint.setAntiAlias(true);
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
        Paint outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, outlinePaint);
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
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(35.0f);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
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

    /**
     *  draws a coin on the screen
     *
     * @param canvas
     * @param x top left corner of the coin
     * @param y top left corner of the coin
     * @param coinDesign 'C' design on coin
     */
    protected void drawCoin(Canvas canvas, float x, float y, String coinDesign) {
        //draws the circle for the gold coin
        Paint gold = new Paint();
        gold.setColor(Color.YELLOW);
        canvas.drawCircle(x + 175.0f, y + 250.0f, 60.0f, gold);

        //draws the text for the fighter's stats
        Paint statText = new Paint();
        statText.setColor(Color.BLACK);
        statText.setTextSize(50);
        statText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(coinDesign, x + 175.0f, y + 270.0f, statText);
    }
}
