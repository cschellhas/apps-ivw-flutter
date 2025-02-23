#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint ivw_flutter.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'ivw_flutter'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '12.0'
  s.ios.framework = 'AdSupport'
  
  #s.xcconfig = { 'OTHER_LDFLAGS' => '-framework INFOnlineLibrary' }
  #s.xcconfig = { 'FRAMEWORK_SEARCH_PATHS' => '${PODS_ROOT}/infonline/INFOnlineLibrary/$(PLATFORM_NAME)/' }

  #s.script_phase = { :name => 'RunInfonline', :script => '/bin/sh "$PROJECT_DIR/infonline/INFOnlineLibrary/copy-framework.sh"' }

  #s.dependency 'IOMbLibrary'


  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'

  
end
