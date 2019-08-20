package alexander.ivanov.messageSystem.services;

public interface FeService extends Service {
    void auth(String name, String password);
    void registration(String name, String password);
}
