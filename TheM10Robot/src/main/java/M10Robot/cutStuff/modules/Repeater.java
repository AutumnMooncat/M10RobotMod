//package M10Robot.cutStuff.modules;
//
//import M10Robot.M10RobotMod;
//import M10Robot.cutStuff.AbstractModuleCard;
//import M10Robot.characters.M10Robot;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import static M10Robot.M10RobotMod.makeCardPath;
//
//public class Repeater extends AbstractModuleCard {
//
//    // TEXT DECLARATION
//
//    public static final String ID = M10RobotMod.makeID(Repeater.class.getSimpleName());
//    public static final String IMG = makeCardPath("Repeater.png");
//    //private static ArrayList<TooltipInfo> boosterExplanation;
//
//    // /TEXT DECLARATION/
//
//
//    // STAT DECLARATION
//
//    private static final CardRarity RARITY = CardRarity.UNCOMMON;
//    private static final CardTarget TARGET = CardTarget.NONE;
//    private static final CardType TYPE = CardType.POWER;
//    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;
//
//    // /STAT DECLARATION/
//
//
//    public Repeater() {
//        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
//        //This should ensure the card can take any booster
//        damage = baseDamage = block = baseBlock = magicNumber = baseMagicNumber = 1;
//    }
//
//    /*
//    @Override
//    public List<TooltipInfo> getCustomTooltipsTop() {
//        if (boosterExplanation == null)
//        {
//            boosterExplanation = new ArrayList<>();
//            boosterExplanation.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
//        }
//        List<TooltipInfo> compoundList = new ArrayList<>(boosterExplanation);
//        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
//        return compoundList;
//    }*/
//
//    // Actions the card should do.
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {}
//
//    //Upgraded stats.
//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            selfRetain = true;
//            rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
//        }
//    }
//
//    @Override
//    public void onEquip() {}
//
//    @Override
//    public void onRemove() {}
//}
