public interface Subject {
  void attach(Kasutaja obj);
  void detach(Kasutaja obj);
  void notify(Kasutaja obj);
}
