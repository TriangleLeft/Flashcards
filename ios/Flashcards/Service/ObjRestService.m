//
//  ObjRestService.m
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import "ObjRestService.h"
#import <AFNetworking/AFNetworking.h>
#import <AFNetworking/AFURLResponseSerialization.h>
#import "GsonResponseSerializer.h"
#import "IOSClass.h"
#import "FlashcardsError.h"

#ifdef APPIUM
NSString* const RestServiceUrl = @"http://localhost:8080";
NSString* const TranslationServiceUrl = @"http://localhost:8080";
#else
NSString* const RestServiceUrl = @"https://www.duolingo.com";
NSString* const TranslationServiceUrl = @"https://d2.duolingo.com";
#endif

@interface AFCall : Call
@property (strong, nonatomic) NSURLRequest *request;
@property (strong, nonatomic) AFHTTPResponseSerializer *serializer;
@end

@implementation AFCall

-(instancetype)initWithRequest:(NSURLRequest *)request serializer:(AFHTTPResponseSerializer *)serializer {
    _request = request;
    _serializer = serializer;
    
    return self;
}

- (void)enqueueWithAction:(id<Action>)onData
               withAction:(id<Action>)onError {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager new];
    if (_serializer) {
        manager.responseSerializer = _serializer;
    }
    
    [[manager dataTaskWithRequest:_request completionHandler:^(NSURLResponse * _Nonnull response, id  _Nullable responseObject, NSError * _Nullable error) {
        
        if (!error) {
            [onData callWithId:responseObject];
        } else {
            if ([error.domain isEqualToString:FlashcardsErrorDomain] && error.code == FlashcardsConversionError) {
                [onError callWithId: [ConversionException new]];
            } else {
                [onError callWithId:[NetworkException new]];
                }
            
        }
    }] resume];
}

- (void)cancel {
    
}

@end

@interface ObjRestService()

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL *)url body:(id)body;

@property (strong, nonatomic) ComGoogleGsonGson* gson;

@end

@implementation ObjRestService

-(instancetype)initWithGson:(ComGoogleGsonGson *)gson {
    _gson = gson;

    return self;
}

- (Call *)loginWithLoginRequestController:(LoginRequestController *)model {
    NSURLRequest *req = [self requestWithMethod:@"POST" url:[self urlWithPath:[RestService PATH_LOGIN]] body:model];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:LoginResponseModel_class_() gson:_gson]];
}

- (Call *)getVocabularyListWithLong:(jlong)timestamp {
    NSString* timestampString = [NSString stringWithFormat:@"%lld", timestamp];
    NSArray<NSURLQueryItem *> *params = @[[[NSURLQueryItem alloc] initWithName:[RestService QUERY_TIMESTAMP] value:timestampString]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:[self urlWithPath:[RestService PATH_VOCABULARY] queryParams:params]];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:VocabularyResponseModel_class_() gson:_gson]];

}

- (Call *)getFlashcardDataWithInt:(jint)count
                              withBoolean:(jboolean)allowPartialDeck
                                 withLong:(jlong)timestamp {
    NSString* timestampString = [NSString stringWithFormat:@"%lld", timestamp];
    NSURLQueryItem *query_timestamp = [[NSURLQueryItem alloc] initWithName:[RestService QUERY_TIMESTAMP] value:timestampString];
    NSString* countString = [NSString stringWithFormat:@"%d", count];
    NSURLQueryItem *query_count = [[NSURLQueryItem alloc] initWithName:[RestService QUERY_FLASHCARDS_COUNT] value:countString];
    NSString* partialString = allowPartialDeck ? @"true" : @"false";
    NSURLQueryItem *query_partial =  [[NSURLQueryItem alloc] initWithName:[RestService QUERY_ALLOW_PARTIAL_DECK] value:partialString];
    NSArray<NSURLQueryItem *> *params = @[query_count, query_partial, query_timestamp];
    
    NSURLRequest *req = [self requestWithMethod:@"GET" url:[self urlWithPath:[RestService PATH_FLASHCARDS] queryParams:params]];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:FlashcardResponseModel_class_() gson:_gson]];
}

- (Call *)postFlashcardResultsWithFlashcardResultsController:(FlashcardResultsController *)model {
    NSURLRequest *req = [self requestWithMethod:@"POST" url:[self urlWithPath:[RestService PATH_FLASHCARDS]] body:model];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:LanguageDataModel_class_() gson:_gson]];
}

- (Call *)switchLanguageWithSwitchLanguageController:(SwitchLanguageController *)controller {
    NSURLRequest *req = [self requestWithMethod:@"POST" url:[self urlWithPath:[RestService PATH_SWITCH_LANGUAGE]] body:controller];
    return [[AFCall alloc] initWithRequest:req serializer:nil];
}

- (Call *)getUserDataWithNSString:(NSString *)userId {
    NSArray<NSURLQueryItem *> *params = @[[[NSURLQueryItem alloc] initWithName:[RestService QUERY_USERID] value:userId]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:[self urlWithPath:[RestService PATH_USERDATA] queryParams:params]];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:UserDataModel_class_() gson:_gson]];
}

- (Call *)getTranslationWithNSString:(NSString *)languageIdFrom
                                withNSString:(NSString *)languageIdTo
                                withNSString:(NSString *)tokens {
    NSString* urlString = [[NSString alloc] initWithFormat:@"%@/%@/%@/%@", TranslationServiceUrl, [TranslationService PATH_TRANSLATION], languageIdFrom, languageIdTo];
    NSURLComponents *components = [[NSURLComponents alloc] initWithString:urlString];
    components.queryItems = @[[[NSURLQueryItem alloc] initWithName:[TranslationService QUERY_TOKENS] value:tokens]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:components.URL];
    return [[AFCall alloc] initWithRequest:req serializer:[[GsonResponseSerializer alloc] initWithClass:WordTranslationModel_class_() gson:_gson]];
}

- (NSURL *)urlWithPath:(NSString *)path {
    return [self urlWithPath:path queryParams:nil];
}

- (NSURL *)urlWithPath:(NSString *)path queryParams:(NSArray<NSURLQueryItem *> *)queryParams {
    NSURLComponents *components = [[NSURLComponents alloc] initWithString:RestServiceUrl];
    components.path = path;
    if (queryParams) {
        components.queryItems = queryParams;
    }
    return components.URL;
}

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL*)url {
    return [self requestWithMethod:method url:url body:nil];
}

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL*)url body:(id)body {
    NSMutableURLRequest *req = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData  timeoutInterval:60];
    [req setHTTPMethod:method];
    [req setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    if (body) {
        [req setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [req setHTTPBody:[[[self gson] toJsonWithId:body] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    return req;
}

@end
