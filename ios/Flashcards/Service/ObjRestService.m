//
//  ObjRestService.m
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import "ObjRestService.h"
#import <AFNetworking/AFNetworking.h>
#import "GsonResponseSerializer.h"
#import "IOSClass.h"

typedef void(^RxSubscriberHandler)(RxSubscriber *subscriber);

@interface MyClosure : NSObject <RxObservable_OnSubscribe>
@property (nonatomic, copy) RxSubscriberHandler handler;
@end
@implementation MyClosure


-(instancetype)initWithClosure:(RxSubscriberHandler)closure {
    _handler = closure;
    return self;
}

- (void)callWithId:(id)t {
    NSLog(@"Called with %@", t);
    RxSubscriber* subsriber = (RxSubscriber*)t;
    _handler(subsriber);
}

@end

@interface ObjRestService()

- (NSURLRequest *)requestWithMethod:(NSString *)method path:(NSString *)path body:(id)body;

@property (strong, nonatomic) ComGoogleGsonGson* gson;

@end

@implementation ObjRestService

-(instancetype)initWithGson:(ComGoogleGsonGson *)gson {
    _gson = gson;
    
    return self;
}

- (RxObservable *)loginWithLoginRequestController:(LoginRequestController *)model {
    NSURLRequest *req = [self requestWithMethod:@"POST" path:[RestService POST_LOGIN] body:model];
    return [self observableWithRequest:req responseModelClass:LoginResponseModel_class_()];
}

- (RxObservable *)getVocabularyListWithLong:(jlong)timestamp {
    // GET
    // class
    return NULL;
}

- (RxObservable *)getFlashcardDataWithInt:(jint)count
                              withBoolean:(jboolean)allowPartialDeck
                                 withLong:(jlong)timestamp {
    return NULL;
}

- (RxObservable *)postFlashcardResultsWithFlashcardResultsController:(FlashcardResultsController *)model {
    NSURLRequest *req = [self requestWithMethod:@"POST" path:[RestService POST_FLASHCARDS] body:model];
    return NULL;
}

- (RxObservable *)switchLanguageWithNSString:(NSString *)languageId {
    return NULL;
}

- (RxObservable *)getUserDataWithNSString:(NSString *)userId {
    return NULL;
}

- (RxObservable *)getTranslationWithNSString:(NSString *)languageIdFrom
                                withNSString:(NSString *)languageIdTo
                                withNSString:(NSString *)tokens {
    return NULL;
}

- (RxObservable *)observableWithRequest:(NSURLRequest *)request responseModelClass:(IOSClass *)clazz {
    RxObservable* observable = [RxObservable createWithRxObservable_OnSubscribe:[[MyClosure alloc] initWithClosure:^(RxSubscriber *subscriber) {
        AFHTTPSessionManager *manager = [AFHTTPSessionManager new];
        manager.responseSerializer = [[GsonResponseSerializer alloc] initWithClass:clazz gson:_gson];
        
        [[manager dataTaskWithRequest:request completionHandler:^(NSURLResponse * _Nonnull response, id  _Nullable responseObject, NSError * _Nullable error) {
            
            if (!error) {
                NSLog(@"Reply JSON: %@", responseObject);
                
                [subscriber onNextWithId:responseObject];
            } else {
                NSLog(@"Error: %@, %@, %@", error, response, responseObject);
            }
        }] resume];
        
    }]];
    return observable;
}

- (NSURLRequest *)requestWithMethod:(NSString *)method path:(NSString*)path body:(id)body {
    NSURLComponents *components = [[NSURLComponents alloc] init];
    components.scheme = [RestService BASE_SCHEME];
    components.host = [RestService BASE_URL];
    components.path = path;
    
    NSMutableURLRequest *req = [NSMutableURLRequest requestWithURL:components.URL cachePolicy:NSURLRequestReloadIgnoringCacheData  timeoutInterval:60];
    [req setHTTPMethod:method];
    [req setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [req setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [req setHTTPBody:[[[self gson] toJsonWithId:body] dataUsingEncoding:NSUTF8StringEncoding]];
    return req;
}

@end
