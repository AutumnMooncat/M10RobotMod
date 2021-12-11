//package M10Robot.powers;
//
//import M10Robot.M10RobotMod;
//import basemod.interfaces.CloneablePowerInterface;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.GameActionManager;
//import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
//import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
//import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.localization.PowerStrings;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//import java.lang.reflect.Field;
//
//public class TauntPower extends AbstractPower implements CloneablePowerInterface {
//
//    public static final String POWER_ID = M10RobotMod.makeID("TauntPower");
//    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
//    public static final String NAME = powerStrings.NAME;
//    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
//
//    private byte moveByte;
//    private AbstractMonster.Intent moveIntent;
//    private EnemyMoveInfo move;
//    private int moveBaseDamage;
//    private int moveMultiplier;
//    private boolean moveIsMultiDamage;
//
//    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
//    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
//    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
//    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));
//
//    public TauntPower(AbstractCreature owner, int amount) {
//        this.name = NAME;
//        this.ID = POWER_ID;
//        this.owner = owner;
//        this.amount = amount;
//
//        this.type = PowerType.DEBUFF;
//        this.isTurnBased = true;
//
//        // We load those txtures here.
//        //this.loadRegion("cExplosion");
//        this.loadRegion("nightmare");
//        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
//        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
//
//        updateDescription();
//    }
//
//    @Override
//    public boolean canPlayCard(AbstractCard card) {
//        return card.type == AbstractCard.CardType.ATTACK;
//    }
//
//    public void atEndOfRound() {
//        if (this.amount <= 0) {
//            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
//        } else {
//            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
//        }
//
//    }
//
//    public void onInitialApplication() {
//        if (owner instanceof AbstractMonster) {
//            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
//                public void update() {
//                    if (TauntPower.this.owner instanceof AbstractMonster) {
//                        TauntPower.this.moveByte = ((AbstractMonster)TauntPower.this.owner).nextMove;
//                        TauntPower.this.moveIntent = ((AbstractMonster)TauntPower.this.owner).intent;
//
//                        try {
//                            Field f = AbstractMonster.class.getDeclaredField("move");
//                            f.setAccessible(true);
//                            TauntPower.this.move = (EnemyMoveInfo)f.get(TauntPower.this.owner);
//                            TauntPower.this.moveBaseDamage = TauntPower.this.move.baseDamage;
//                            TauntPower.this.moveMultiplier = TauntPower.this.move.multiplier;
//                            TauntPower.this.moveIsMultiDamage = TauntPower.this.move.isMultiDamage;
//                            TauntPower.this.move.intent = AbstractMonster.Intent.ATTACK;
//                            ((AbstractMonster)TauntPower.this.owner).createIntent();
//                        } catch (NoSuchFieldException | IllegalAccessException var2) {
//                            var2.printStackTrace();
//                        }
//                    }
//
//                    this.isDone = true;
//                }
//            });
//        }
//    }
//
//    public void onRemove() {
//        if (this.owner instanceof AbstractMonster) {
//            AbstractMonster m = (AbstractMonster)this.owner;
//            if (this.move != null) {
//                m.setMove(this.moveByte, this.moveIntent, this.moveBaseDamage, this.moveMultiplier, this.moveIsMultiDamage);
//            } else {
//                m.setMove(this.moveByte, this.moveIntent);
//            }
//
//            m.createIntent();
//            m.applyPowers();
//        }
//
//    }
//
//    public void overrideMove() {
//        this.addToBot(new AnimateSlowAttackAction(owner));
//    }
//
//    public void updateDescription() {
//        if (this.amount == 1) {
//            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
//        } else {
//            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
//        }
//
//    }
//
//    @Override
//    public AbstractPower makeCopy() {
//        return new TauntPower(owner, amount);
//    }
//
//    public static void instrumentTurnOverride(AbstractMonster m) {
//        if (m.hasPower(TauntPower.POWER_ID)) {
//            ((TauntPower)m.getPower(TauntPower.POWER_ID)).overrideMove();
//        }
//    }
//
//    //This stuff is reasonable. Reworked from StunPatches from stslib
//
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "rollMove"
//    )
//    public static class RollMove {
//        public RollMove() {
//        }
//
//        public static SpireReturn<?> Prefix(AbstractMonster __instance) {
//            return __instance.hasPower(TauntPower.POWER_ID) ? SpireReturn.Return(null) : SpireReturn.Continue();
//        }
//    }
//
//    @SpirePatch(
//            clz = GameActionManager.class,
//            method = "getNextAction"
//    )
//    public static class GetNextAction {
//        public GetNextAction() {
//        }
//
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && m.getMethodName().equals("takeTurn")) {
//                        m.replace("if (m.hasPower(M10Robot.powers.TauntPower.POWER_ID)) {" +
//                                "M10Robot.powers.TauntPower.instrumentTurnOverride(m);" +
//                                "} else {" +
//                                "$_ = $proceed($$);" +
//                                "}");
//                    }
//
//                }
//            };
//        }
//    }
//}