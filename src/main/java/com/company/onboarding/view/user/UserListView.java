package com.company.onboarding.view.user;

import com.company.onboarding.entity.OnBoardingStatus;
import com.company.onboarding.entity.User;
import com.company.onboarding.view.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.chartsflowui.component.Chart;
import io.jmix.chartsflowui.data.item.MapDataItem;
import io.jmix.chartsflowui.kit.component.model.DataSet;
import io.jmix.chartsflowui.kit.data.chart.ListChartItems;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Route(value = "users", layout = MainView.class)
@ViewController(id = "User.list")
@ViewDescriptor(path = "user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
public class UserListView extends StandardListView<User> {

    @ViewComponent
    private DataGrid<User> usersDataGrid;

    @ViewComponent
    protected Chart pie;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private DataManager dataManager;

    @Supply(to = "usersDataGrid.picture", subject = "renderer")
    private Renderer<User> usersDataGridPictureRenderer() {
        return new ComponentRenderer<>(user -> {
            FileRef fileRef = user.getPicture();
            if (fileRef != null) {
                Image image = uiComponents.create(Image.class);
                image.setWidth("30px");
                image.setHeight("30px");
                StreamResource streamResource = new StreamResource(
                        fileRef.getFileName(),
                        () -> fileStorage.openStream(fileRef));
                image.setSrc(streamResource);
                image.setClassName("user-picture");

                return image;
            } else {
                return null;
            }
        });
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        final Integer notStarted = dataManager.loadValue(
                        "select count(u) from User u where u.onboardingStatus = :onboardingStatus",
                        Integer.class
                )
                .parameter("onboardingStatus", OnBoardingStatus.NOT_STARTED)
                .one();

        final Integer inProgress = dataManager.loadValue(
                        "select count(u) from User u where u.onboardingStatus = :onboardingStatus",
                        Integer.class
                )
                .parameter("onboardingStatus", OnBoardingStatus.IN_PROGRESS)
                .one();


        final Integer completed = dataManager.loadValue(
                        "select count(u) from User u where u.onboardingStatus = :onboardingStatus",
                        Integer.class
                )
                .parameter("onboardingStatus", OnBoardingStatus.COMPLETED)
                .one();
        ListChartItems<MapDataItem> items = new ListChartItems<>(
                new MapDataItem(Map.of("category", "Completed", "value", notStarted)),
                new MapDataItem(Map.of("category", "Not Started", "value", inProgress)),
                new MapDataItem(Map.of("category", "In Progress", "value", completed))
        );


        pie.setDataSet(
                new DataSet()
                        .withSource(
                                new DataSet.Source<MapDataItem>()
                                        .withDataProvider(items)
                                        .withCategoryField("category")
                                        .withValueField("value")
                        )
        );
    }
}