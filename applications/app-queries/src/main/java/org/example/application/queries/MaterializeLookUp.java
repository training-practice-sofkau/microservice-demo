package org.example.application.queries;

import org.example.application.queries.adapter.repo.AccountModeView;
import org.example.application.queries.adapter.repo.AccountRepository;
import org.example.application.queries.adapter.repo.TransactionModelView;
import org.example.domain.events.AccountCreated;
import org.example.domain.events.TransactionAdded;
import org.example.generic.DelegateService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class MaterializeLookUp {
    private final Map<String, Flux<DelegateService>> business = new HashMap<>();

    public MaterializeLookUp(ApplicationContext context, AccountRepository repository) {
        business.put("org.example.AccountCreated", Flux.just( input -> {
            var event = (AccountCreated)input;
            var document = new AccountModeView();
            document.setId(event.aggregateRootId());
            document.setName(event.getName().value());
            document.setUserId(event.getUserId().value());
            return repository.save(document).then();
        }));

        business.put("org.example.TransactionAdded", Flux.just( input -> {
            var event = (TransactionAdded)input;
            return repository.findById(event.aggregateRootId()).flatMap(doc ->{

                var transactionModelView = new TransactionModelView();
                transactionModelView.setDate(event.getDate());
                transactionModelView.setId(event.getId().value());
                transactionModelView.setName(event.getName().value());
                var trans =  doc.getTransactionModelViews();
                trans.put(event.getId().value(), transactionModelView);
                doc.setTransactionModelViews(trans);

                return repository.save(doc).then();
            });
        }));
    }


    public Flux<DelegateService> get(String eventType) {
        return business.getOrDefault(eventType, Flux.empty());
    }
}
