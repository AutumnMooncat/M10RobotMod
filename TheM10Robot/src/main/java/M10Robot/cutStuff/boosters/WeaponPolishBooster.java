//package M10Robot.cutStuff.boosters;
//
//import M10Robot.M10RobotMod;
//import M10Robot.cutStuff.modifiers.AbstractBoosterModifier;
//import M10Robot.cutStuff.modifiers.TempDamageModifier;
//import M10Robot.cutStuff.AbstractBoosterCard;
//import M10Robot.cards.tempCards.TempCard;
//import M10Robot.characters.M10Robot;
//import M10Robot.cutStuff.patches.BoundFieldPatch;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.function.Predicate;
//
//import static M10Robot.M10RobotMod.makeCardPath;
//
//public class WeaponPolishBooster extends AbstractBoosterCard implements TempCard {
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
//    public static final String ID = M10RobotMod.makeID(WeaponPolishBooster.class.getSimpleName());
//    public static final String IMG = makeCardPath("PlaceholderSkill.png");
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
//    private static final int DAMAGE = 1;
//    private static final int UPGRADE_PLUS_DAMAGE = 2;
//    int amount = 0;
//
//    // /STAT DECLARATION/
//
//
//    public WeaponPolishBooster() {
//        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
//        damage = baseDamage = DAMAGE;
//        BoundFieldPatch.boundField.set(this, true);
//    }
//
//    public WeaponPolishBooster(int amount) {
//        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
//        damage = baseDamage = amount;
//        this.amount = amount;
//        BoundFieldPatch.boundField.set(this, true);
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
//        return new ArrayList<>(Collections.singletonList(new TempDamageModifier(damage)));
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
//
//    @Override
//    public AbstractCard makeStatEquivalentCopy() {
//        return new WeaponPolishBooster(amount);
//    }
//}
