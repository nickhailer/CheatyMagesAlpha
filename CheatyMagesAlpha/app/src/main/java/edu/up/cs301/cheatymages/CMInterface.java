package edu.up.cs301.cheatymages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import edu.up.cs301.cheatymages.Actions.BetAction;
import edu.up.cs301.cheatymages.Actions.DetectMagicAction;
import edu.up.cs301.cheatymages.Actions.DiscardCardsAction;
import edu.up.cs301.cheatymages.Actions.PassAction;
import edu.up.cs301.cheatymages.CMGameState;

public class CMInterface extends SurfaceView {//implements View.OnClickListener{
    private float cardHeight = 240;
    private float cardWidth = 180;
    public CMGameState gameState = new CMGameState(4);
    public String judgeType;

    public CMInterface(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);

        //makes the surface view background to transparent
        //code is adapted from https://stackoverflow.com/a/27959612
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    protected void onDraw(Canvas canvas){
        // Determines judges judgement
        if(gameState.getJudge().getJudgementType() == 'd') {
            judgeType = "Dispel";
        }
        else {
            judgeType = "Eject";
        }
        // Draws judge card
        drawJudgeCard(canvas, 15, 120, gameState.getJudge().getName(), gameState.getJudge().getManaLimit(),
                        judgeType, gameState.getJudge().getDisallowedSpells());

        // Draws 5 random fighter cards
        for(int i=0; i<gameState.getFighters().length; i++) {
            drawFighterCard(canvas, 50, 430 + (300*i), gameState.getFighters()[i].getName(),
                    gameState.getFighters()[i].getPower(), gameState.getFighters()[i].getPrizeMoney(),true);
            //fix
        }

        // Draws hand of spell cards
        for(int i=0; i<gameState.getHands()[i].size(); i++) {
            drawSpellCard(canvas, 50 + (200*i), 2000, gameState.getHands()[i].get(i).getName(), gameState.getHands()[i].get(i).getMana(), gameState.getHands()[i].get(i).getSpellType(), gameState.getHands()[i].get(i).getPowerMod(),
                    false, "", false);
        }

        // Draws played spell cards
        // figure out y coordinates------------
        for(int i=0; i<gameState.getAttachedSpells()[i].size(); i++) {
            if(gameState.getAttachedSpells()[i].get(i).getSpellType() == 'e') {
                drawFaceDownCard(canvas, 300 + (250*i), 1030, Color.GRAY);
            }
            else {
                drawSpellCard(canvas, 300 + (250*i), 430, gameState.getAttachedSpells()[i].get(i).getName(), gameState.getAttachedSpells()[i].get(i).getMana(),
                        gameState.getAttachedSpells()[i].get(i).getSpellType(), gameState.getAttachedSpells()[i].get(i).getPowerMod(),
                        false, "", false);
            }
        }

        for(int i=0; i<gameState.getAttachedSpells()[i].size(); i++) {

        }

        drawSpellCard(canvas, 300, 430, "Healing", 1, 'd',+4,
                false, "", false);
        drawSpellCard(canvas, 300, 730, "Blizzard", 4, 'd',-6,
                false, "", false);
        drawFaceDownCard(canvas, 300, 1030, Color.GRAY);

        // draws coin label
        drawCoin(canvas, 1400, -45, "C");
    }

    //draws a fighter card on the screen
    //x and y are the top left corner of the card
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

    //draws a spell card on the screen
    //if hasCardText is true the card is printed with textEffect at the bottom
    //else if hasCardText is false the card only has a power modifier
    //spell types: d = direct, e = enchant, s = support
    protected void drawSpellCard(Canvas canvas, float x, float y, String spellName, int mana,
                                 char spellType, int powerMod, boolean hasCardText,
                                 String effectText, boolean isForbidden){
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

    //draws a judge card on the screen
    //disallows shows which card types this judge bans
    // In order: direct, enchant, support, forbidden
    protected void drawJudgeCard(Canvas canvas, float x, float y, String judgeName, int manaLimit,
                                 String judgementType, ArrayList<Character> disallows) {

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, judgeName);

        //writes the mana limit on the card
        Paint manaLimitTextPaint = new Paint();
        manaLimitTextPaint.setColor(Color.BLACK);
        manaLimitTextPaint.setTextAlign(Paint.Align.CENTER);
        manaLimitTextPaint.setTextSize(30.0f);
        manaLimitTextPaint.setAntiAlias(true);
        canvas.drawText(Integer.toString(manaLimit), x + 30.0f,y + cardHeight - 75.0f,
                manaLimitTextPaint);

        //writes judgement type on the card
        Paint judgementTextPaint = new Paint();
        judgementTextPaint.setColor(Color.BLACK);
        judgementTextPaint.setTextSize(30.0f);
        manaLimitTextPaint.setTextAlign(Paint.Align.RIGHT);
        judgementTextPaint.setAntiAlias(true);
        canvas.drawText(judgementType, x + cardWidth - 90.0f,y + cardHeight - 75.0f,
                judgementTextPaint);

        //draws symbols of cards this judge disallows at bottom of card
        Paint symbolPaint = new Paint();
        int symbolsDrawn = 0;
        for(int i = 0; i < 4; i++) {
            if (disallows.get(i) == 'd') {
                symbolPaint.setColor(Color.RED);
            } else if (disallows.get(i) == 'e') {
                symbolPaint.setColor(Color.BLUE);
            } else if (disallows.get(i) == 's') {
                symbolPaint.setColor(0xFFFFD700);
            } else {
                symbolPaint.setColor(Color.GREEN);
            }

                canvas.drawCircle(x + 25.0f + symbolsDrawn * 25.0f, y + cardHeight - 40.0f,
                        12.5f, symbolPaint);
                symbolsDrawn++;

        }

    }

    //draws a face down card on the screen
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

    //draws the black border for the card outline
    protected void drawCardOutline(Canvas canvas, float x, float y){
        Paint outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, outlinePaint);
    }

    //draws the card name at the top of the card
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(35.0f);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        canvas.drawText(text, x + cardWidth/2, y + 50.0f, titlePaint);
    }

    //converts a string into an array of smaller strings using a max pixel width
    //this is intended be used to wrap text within a certain width
    //this code is functional for now but will cut off words and will need to be altered
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


    //draws a coin on the screen
    //x and y are the top left corner of the coin
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
    /*
    @Override
    public void onClick(View button) {
        if(button instanceof ImageButton) {
            BetAction betAction = new BetAction(gameState.getPlayerTurn(), gameState.getBets());
            this.game.sendAction(rollAction);
        }
        else if(button instanceof ImageButton) {
            DetectMagicAction detectMagicAction = new DetectMagicAction(gameState.getPlayerTurn(), gameState.getFighters());
        }
        else if(button instanceof ImageButton) {
            PassAction passAction = new PassAction(gameState);
        }
        else {
            DiscardCardsAction discardCardsAction = new DiscardCardsAction();
            this.game.sendAction(holdAction);
        }
    }

     */
}
