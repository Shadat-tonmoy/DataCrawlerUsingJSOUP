package EnglishWordCollections;

import java.util.List;

public class ListOfWord
{
    private List<Word> wordList;

    public ListOfWord() {
    }

    public ListOfWord(List<Word> wordList) {
        this.wordList = wordList;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    @Override
    public String toString() {
        return "ListOfWord{" +
                "wordList=" + wordList +
                '}';
    }
}
