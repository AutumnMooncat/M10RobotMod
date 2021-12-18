//package M10Robot.cutStuff.boosters;
//
//import M10Robot.M10RobotMod;
//import M10Robot.cutStuff.modifiers.AbstractBoosterModifier;
//import M10Robot.cutStuff.modifiers.TempBypassModifier;
//import M10Robot.cutStuff.AbstractBoosterCard;
//import M10Robot.cards.uniqueCards.UniqueCard;
//import M10Robot.characters.M10Robot;
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
//public class SecretRootkit extends AbstractBoosterCard implements UniqueCard {
//
//
//    // TEXT DECLARATION
//
//    public static final String ID = M10RobotMod.makeID(SecretRootkit.class.getSimpleName());
//    public static final String IMG = makeCardPath("SecretRootkit.png");
//
//    // /TEXT DECLARATION/
//
//
//    // STAT DECLARATION
//
//    private static final CardRarity RARITY = CardRarity.RARE;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.SKILL;
//    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;
//
//    private static final int REPEAT = 1;
//
//    // /STAT DECLARATION/
//
//
//    public SecretRootkit() {
//        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
//        magicNumber = baseMagicNumber = REPEAT;
//    }
//
//    // Actions the card should do.
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {}
//
//    @Override
//    public Predicate<AbstractCard> getFilter() {
//        return greaterThanZeroCost;
//    }
//
//    @Override
//    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
//        return new ArrayList<>(Collections.singletonList(new TempBypassModifier(magicNumber)));
//    }
//
//    //Upgraded stats.
//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            this.selfRetain = true;
//            rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
//        }
//    }
//}
