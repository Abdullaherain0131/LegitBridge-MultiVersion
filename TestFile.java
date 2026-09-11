public class TestFile {
    public void doSomething() {
        dummyPlayer.getInventory().clone(
//? if <=1.19.2 {
/*client.player.inventory*/
//?} else {
client.player.getInventory()
//?}
        );
    }
}
