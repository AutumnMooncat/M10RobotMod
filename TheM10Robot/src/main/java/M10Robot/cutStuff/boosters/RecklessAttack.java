//package M10Robot.cutStuff.boosters;
//
//import M10Robot.M10RobotMod;
//import M10Robot.cutStuff.AbstractBoosterCard;
//import M10Robot.characters.M10Robot;
//import M10Robot.cutStuff.modifiers.AbstractBoosterModifier;
//import M10Robot.cutStuff.modifiers.GainRecoilEffect;
//import M10Robot.cutStuff.modifiers.TempDamageModifier;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.function.Predicate;
//
//import static M10Robot.M10RobotMod.makeCardPath;
//
//public class RecklessAttack extends AbstractBoosterCard {
//
//    /*
//     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
//     *
//     * Defend Gain 5 (8) block.
//     */
//
//
//    // TEXT DECLARATION
//
//    public static final String ID = M10RobotMod.makeID(RecklessAttack.class.getSimpleName());
//    public static final String IMG = makeCardPath("RecklessAttack.png");
//
//    // /TEXT DECLARATION/
//
//
//    // STAT DECLARATION
//
//    private static final CardRarity RARITY = CardRarity.UNCOMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.SKILL;
//    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;
//
//    private static final int LOSS = 1;
//    private static final int DAMAGE = 8;
//    private static final int UPGRADE_PLUS_DAMAGE = 4;
//
//    // /STAT DECLARATION/
//
//
//    public RecklessAttack() {
//        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
//        damage = baseDamage = DAMAGE;
//        magicNumber = baseMagicNumber = LOSS;
//    }
//
//    // Actions the card should do.
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {}
//
//    @Override
//    public Predicate<AbstractCard> getFilter() {
//        return hasDamageValue;
//    }
//
//    @Override
//    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
//        return new ArrayList<>(Arrays.asList(new TempDamageModifier(damage), new GainRecoilEffect(this)));
//    }
//
//    //Upgraded stats.
//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            upgradeDamage(UPGRADE_PLUS_DAMAGE);
//            initializeDescription();
//        }
//    }
//}
