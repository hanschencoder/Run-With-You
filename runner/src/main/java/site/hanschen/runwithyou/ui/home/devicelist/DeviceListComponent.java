package site.hanschen.runwithyou.ui.home.devicelist;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = DeviceListModule.class)
interface DeviceListComponent {

    void inject(DeviceFragment fragment);
}
