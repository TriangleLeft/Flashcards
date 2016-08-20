//
//  GsonResponseSerializer.h
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AFNetworking/AFNetworking.h>

@class IOSClass;
@class ComGoogleGsonGson;

@interface GsonResponseSerializer : AFHTTPResponseSerializer

-(instancetype)initWithClass:(IOSClass *)class gson:(ComGoogleGsonGson *)gson;

@end
