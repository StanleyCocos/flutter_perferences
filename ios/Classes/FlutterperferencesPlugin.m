#import "FlutterperferencesPlugin.h"
#if __has_include(<flutterperferences/flutterperferences-Swift.h>)
#import <flutterperferences/flutterperferences-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutterperferences-Swift.h"
#endif

@implementation FlutterperferencesPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterperferencesPlugin registerWithRegistrar:registrar];
}
@end
