package brachy.modularui.widgets.menu;

import brachy.modularui.api.layout.IViewportStack;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.viewport.ModularGuiContext;
import brachy.modularui.widget.WidgetTree;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

import java.util.List;

@ApiStatus.Experimental
public class MenuPanel extends ModularPanel<MenuPanel> {

    public MenuPanel(String name, IWidget menu) {
        super(name);
        fullScreenInvisible();
        child(menu);
        themeOverride("modularui.context_menu");
    }

    public void openSubMenu(IWidget menuList) {
        child(menuList);
    }

    @Override
    public void onClose() {
        super.onClose();
        // close all menus that are related to this panel
        closeAllMenus(false, false);
    }

    @Override
    protected void onChildAdd(IWidget child) {
        super.onChildAdd(child);
        child.scheduleResize();
    }

    @Override
    public void transform(IViewportStack stack) {
        ModularGuiContext context = getContext();
        Matrix4f pose = context.getLastGraphicsPose();
        stack.translate(pose.m30(), pose.m31());
        super.transform(stack);
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public boolean closeOnOutOfBoundsClick() {
        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void closeAllMenus(boolean soft, boolean requireNoHover) {
        // need to collect menus first instead of closing while iterating to avoid CME
        List<Menu> menus = WidgetTree.flatListByType(this, Menu.class);
        for (Menu<?> menu : menus) {
            menu.checkClose(soft, requireNoHover);
        }
    }
}
