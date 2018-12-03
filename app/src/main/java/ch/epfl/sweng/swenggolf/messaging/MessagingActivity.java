package ch.epfl.sweng.swenggolf.messaging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.answer.Answer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class MessagingActivity extends FragmentConverter {
    private View inflated;
    private String offerId;
    private User otherUser;
    private MessagesAdapter messagesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        otherUser = bundle.getParcelable(User.USER);
        offerId = bundle.getString(Offer.OFFER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, otherUser.getUserName());
        inflated = inflater.inflate(R.layout.activity_messaging, container, false);
        setRecyclerView();
        inflated.findViewById(R.id.send_message_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return inflated;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.messages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchMessages();

        messagesAdapter = new MessagesAdapter(otherUser);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(messagesAdapter);
    }

    private void sendMessage() {
        EditText editText = findViewById(R.id.message_content);
        Answers messages = messagesAdapter.getAnswers();
        messages.getAnswerList()
                .add(new Answer(Config.getUser().getUserId(), editText.getText().toString()));
        messagesAdapter.setAnswers(messages);
        // TODO we write all answers each time ?!
        Database.getInstance().write(Database.MESSAGES_PATH, offerId, messages);
        editText.getText().clear();
        messagesAdapter.notifyDataSetChanged();
    }

    private void fetchMessages() {
        ValueListener<Answers> answerListener = new ValueListener<Answers>() {
            @Override
            public void onDataChange(Answers value) {
                if (value != null) {
                    messagesAdapter.setAnswers(value);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                // TODO put a toast or something?
            }
        };
        Database.getInstance().read(Database.MESSAGES_PATH, offerId,
                answerListener, Answers.class);
    }


}
